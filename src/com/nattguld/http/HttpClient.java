package com.nattguld.http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.SSLHandshakeException;

import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.cookies.CookieJar;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.rotating.ProxyUsageMonitor;
import com.nattguld.http.proxies.rotating.RotatingProxy;
import com.nattguld.http.requests.ContentRequest;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.requests.impl.OptionsRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.http.response.ResponseStatus;
import com.nattguld.http.response.bodies.IResponseBody;
import com.nattguld.http.response.bodies.ResponseBodyParser;
import com.nattguld.http.response.bodies.impl.FileResponseBody;
import com.nattguld.http.response.bodies.impl.StringResponseBody;
import com.nattguld.http.response.decode.impl.HeaderDecoder;
import com.nattguld.http.sec.ConnectionSecurityHandler;
import com.nattguld.http.socket.HttpSocket;
import com.nattguld.http.util.NetUtil;

/**
 * 
 * @author randqm
 *
 */

public class HttpClient implements AutoCloseable {
	
	/**
	 * The proxy to use.
	 */
	private final HttpProxy proxy;
	
	/**
	 * The browser configurations.
	 */
	private final Browser browser;
	
	/**
	 * The cookie jar.
	 */
	private final CookieJar cookieJar;
	
	/**
	 * The connection policies.
	 */
	private final List<ConnectionPolicy> policies;
	
	/**
	 * The connection security handler if required.
	 */
	private ConnectionSecurityHandler conSecHandler;
	
	/**
	 * The last GET url.
	 */
	private String lastGetUrl;
	
	/**
	 * The amount of redirects on 1 request.
	 */
	private int redirects;
	
	/**
	 * The client user.
	 */
	private String user;
	
	
	/**
	 * Creates a new default HTTP connection.
	 * 
	 * @param userIdentity The user identifier.
	 * 
	 * @param policies The connection policies.
	 */
	public HttpClient(ConnectionPolicy... policies) {
		this(new Browser(false), policies);
	}
	
	/**
	 * Creates a HTTP connection.
	 * 
	 * @param userIdentity The user identifier.
	 * 
	 * @param proxy The proxy.
	 * 
	 * @param policies The connection policies.
	 */
	public HttpClient(HttpProxy proxy, ConnectionPolicy... policies) {
		this(new Browser(false), proxy, policies);
	}
	
	/**
	 * Creates a HTTP connection.
	 * 
	 * @param userIdentity The user identifier.
	 * 
	 * @param browser The browser.
	 * 
	 * @param policies The connection policies.
	 */
	public HttpClient(Browser browser, ConnectionPolicy... policies) {
		this(browser, null, policies);
	}
	
	/**
	 * Creates a HTTP connection.
	 * 
	 * @param proxy The proxy.
	 * 
	 * @param browser The browser.
	 * 
	 * @param policies The connection policies.
	 */
	public HttpClient(Browser browser, HttpProxy proxy, ConnectionPolicy... policies) {
		this.proxy = proxy;
		this.browser = browser;
		this.cookieJar = new CookieJar();
		this.policies = new ArrayList<>();
		this.conSecHandler = new ConnectionSecurityHandler();
		
		if (Objects.nonNull(policies) && policies.length > 0) {
			for (ConnectionPolicy policy : policies) {
				this.policies.add(policy);
			}
		}
	}
	
	/**
	 * Initializes the proxies for the client.
	 * 
	 * @param user The user.
	 */
	public HttpClient initProxies(String user) {
		this.user = user;
		
		if (Objects.nonNull(proxy) && proxy instanceof RotatingProxy) {
			ProxyUsageMonitor.setInUse((RotatingProxy)proxy, user, true);
		}
		return this;
	}
	
	@Override
	public void close() {
		if (Objects.nonNull(proxy) && proxy instanceof RotatingProxy) {
			ProxyUsageMonitor.setInUse((RotatingProxy)proxy, user, false);
		}
	}
	
	/**
	 * Attempts to dispatch a request.
	 * 
	 * @param mainRequest The main request.
	 * 
	 * @param backgroundRequests The background requests.
	 * 
	 * @return The request response.
	 */
	public RequestResponse dispatchRequest(Request mainRequest, Request... backgroundRequests) {
		ExecutorService requestExecutor = Executors.newFixedThreadPool(1 + backgroundRequests.length);
		LinkedList<Callable<RequestResponse>> requestCallables = new LinkedList<>();
		
		requestCallables.add(new Callable<RequestResponse>() {
			@Override
			public RequestResponse call() throws Exception {
				return dispatchRequest(mainRequest);
			}
		});
		for (Request bg : backgroundRequests) {
			requestCallables.add(new Callable<RequestResponse>() {
				@Override
				public RequestResponse call() throws Exception {
					if (bg.getRequestType() == RequestType.GET) {
						((GetRequest)bg).setNoRef(true);
					}
					return dispatchRequest(bg);
				}
			});
		}
		try {
			List<Future<RequestResponse>> requestResponses = requestExecutor.invokeAll(requestCallables);
			
			if (NetConfig.getGlobalInstance().isDebug()) {
				for (Future<RequestResponse> frr : requestResponses) {
					if (NetConfig.getGlobalInstance().isDebug()) {
						System.out.println("Background Request [" + frr.get().getEndpoint() + "]: " + frr.get().getCode());
					}
				}
			}
			return requestResponses.get(0).get();
			
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			return new RequestResponse(mainRequest.getUrl(), new ResponseStatus(0, "Request execution got interrupted")
					, new StringResponseBody(ex.getMessage()), null);
			
		} catch (ExecutionException ex) {
			ex.printStackTrace();
			return new RequestResponse(mainRequest.getUrl(), new ResponseStatus(0, "Failed to execute request")
					, new StringResponseBody(ex.getMessage()), null);
		} finally {
			requestExecutor.shutdown();
		}
	}

	/**
	 * Attempts to dispatch a request.
	 * 
	 * @param request The request.
	 * 
	 * @return The request response.
	 */
	private RequestResponse dispatchRequest(Request request) {
		request.setAttempts(request.getAttempts() + 1);
		
		if (request.getAttempts() > browser.getConnectionAttempts()) {
			RequestResponse tooManyAttemptsResponse = new RequestResponse(request.getUrl(), new ResponseStatus(0, "Too many attempts")
					, new StringResponseBody("Failed to dispatch request (attempts: " + (request.getAttempts()  - 1) + ")"), null);
			request.setAttempts(0);
			return tooManyAttemptsResponse;
		}
		StringBuilder raw = new StringBuilder();
		Headers headers = prepareHeaders(request);
		RequestResponse rr = null;
		
		String host = NetUtil.getDomain(request.getUrl());
		String endpoint = request.isExactEndpoint() ? request.getUrl() : request.getUrl().substring(request.getUrl().indexOf(host) + host.length(), request.getUrl().length());

		if (NetConfig.getGlobalInstance().isDebug()) {
			System.out.println("Host: " + host + ", Endpoint: " + endpoint);
		}
		try (Socket socket = HttpSocket.connect(proxy, host, browser, hasPolicy(ConnectionPolicy.SSL), request.getForcePort())) {
			try (BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, Charset.forName("UTF-8").newEncoder()), true) {
					@Override
					public void println(String s) {
						super.println(s);
						
						if (NetConfig.getGlobalInstance().isDebug()) {
							raw.append(s);
							raw.append(System.lineSeparator());
						}
					}
				};
				writer.println(request.getRequestType().getName() + " " + (endpoint.isEmpty() ? "/" : endpoint) + " " + browser.getHttpVersion().getName());

				for (Entry<String, String> header : headers.getHeaders().entrySet()) {
					writer.println(header.getKey() + ": " + header.getValue());
				}
				writer.println();
				writer.flush();
			
				if (NetConfig.getGlobalInstance().isDebug()) {
					System.err.println(raw.toString());
				}
				if (request.hasBody()) { 
					ContentRequest contReq = ((ContentRequest)request);
					
					if (Objects.nonNull(contReq.getBody().getContent())) {
						contReq.getBody().write(out);
					}
				}
				out.flush();
				
				ResponseStatus rs = null;
				Headers responseHeaders = new Headers();
			
				try (InputStream in = socket.getInputStream()) {
					HeaderDecoder hd = new HeaderDecoder();
					hd.decode(in);

					rs = hd.getResponseStatus();
					responseHeaders = hd.getHeaders();
					
					if (rs == null) {
						System.err.println("Failed to decode request headers for " + request.getUrl());
						return dispatchRequest(request);
					}
					cookieJar.importCookies(hd.getCookies());
					
					IResponseBody<?> responseBody = rs.getCode() != 204 ? ResponseBodyParser.parseResponseBody(request, responseHeaders, in) : new StringResponseBody("");
					rr = new RequestResponse(request.getUrl(), rs, responseBody, responseHeaders);
				}
				writer.close();
			}
		} catch (UnknownHostException ex) {
			System.err.println("Unknown host " + host);
			
		} catch (ConnectException ex) {
			System.err.println("Failed to establish connection. Connection refused.");
			
		} catch (SocketException ex) {
			ex.printStackTrace();
			
		} catch (SocketTimeoutException ex) {
			System.err.println("Failed to establish connection. Timed out.");
			
		} catch (SSLHandshakeException ex) {
			ex.printStackTrace();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (rr == null) {
			return dispatchRequest(request);
		}
		if (rr.getCode() == 400 && request.getCode() != 400 && !request.getUrl().endsWith("/")) {
			System.err.println("Bad request, maybe the url needs to end with a slash? (/)");
		}
		if (request.getRequestType() == RequestType.GET && !((GetRequest)request).isNoRef() && !request.isXMLHttpRequest()) {
			lastGetUrl = request.getUrl();
		}
		if (!hasPolicy(ConnectionPolicy.NO_AUTO_REDIRECT) && 
				(rr.getCode() == 301 || rr.getCode() == 302 || rr.getCode() == 303
				|| rr.getCode() == 307 || rr.getCode() == 308)) {
				
			if (redirects >= 10) {
				redirects = 0;
				request.setAttempts(0);
				return new RequestResponse(request.getUrl(), new ResponseStatus(0, "Too many redirects")
						, new StringResponseBody("Too many redirects through " + request.getUrl()), null);
			}
			String redirectUrl = rr.getLocation();
				
			if (Objects.nonNull(redirectUrl)) {
				if (!redirectUrl.startsWith("http")) {
					if (Objects.isNull(lastGetUrl)) {
						redirects = 0;
						request.setAttempts(0);
						return new RequestResponse(redirectUrl, new ResponseStatus(0, "Failed to redirect")
								, new StringResponseBody("Failed to auto redirect for " + request.getUrl()), null);
					}
					redirectUrl = NetUtil.getBaseUrl(request.getUrl()) + redirectUrl;
				}
				if (NetConfig.getGlobalInstance().isDebug()) {
					System.out.println("Redirect (" + rr.getCode() + ") => " + redirectUrl + " [Last GET: " + lastGetUrl + "][Location: " + rr.getLocation() + "]");	
				}
				if (redirectUrl.equals(request.getUrl())) {
					if (rr.getCode() == 301) {
						policies.add(ConnectionPolicy.SSL);
						redirects++;
						request.setAttempts(0);
						return dispatchRequest(request);
					}
					if (NetConfig.getGlobalInstance().isDebug()) {
						System.out.println("Redirect => " + redirectUrl + " is same as current url");
					}
				}
				redirects++;
				request.setAttempts(0);
				Request redirReq = new GetRequest(redirectUrl, 200, request.getHeaders());
				redirReq.setSavePath(request.getSavePath());
				redirReq.setProgressListener(request.getProgressListener());
				return dispatchRequest(redirReq);
			}
			System.err.println("No redirect url found on a redirect response (" + request.getUrl() + " => " + rr.getCode() + ")");
		}
		redirects = 0;

		if (request instanceof ContentRequest) {
			ContentRequest contReq = ((ContentRequest)request);
			
			if (contReq.hasBody() && contReq.getBody().isChunked()) {
				if (!contReq.getBody().getChunkHandler().isFinished()) {
					request.setAttempts(0);
					return dispatchRequest(request);
				}
			}
		}
		if (conSecHandler.hasImplementations()) {
			if (!conSecHandler.bypass(this, request, rr)) {
				return dispatchRequest(request);
			}
		}
		request.setAttempts(0);
		return rr;
	}
	
	/**
	 * Attempts to download a resource.
	 * 
	 * @param path The save path.
	 * 
	 * @param request The request.
	 * 
	 * @return The saved file.
	 */
	public File download(String path, Request request) {
		long start = System.currentTimeMillis();
		
		request.setSavePath(path);
		RequestResponse rr = dispatchRequest(request);
		
		if (!rr.validate()) {
			return null;
		}
		File f = (File)((FileResponseBody)rr.getResponseBody()).getBody();
		
		if (NetConfig.getGlobalInstance().isDebug()) {
			System.out.println("Downloaded " + f.getName() + " in " + ((System.currentTimeMillis() - start) / 1000) + "s");
		}
		return f;
	}
	
	/**
	 * Prepares & retrieves the request headers.
	 * 
	 * @param request The request.
	 * 
	 * @return The headers.
	 */
	private Headers prepareHeaders(Request request) {
		Headers headers = new Headers();
		
		headers.add("Host", NetUtil.getDomain(request.getUrl()));
		headers.add("Connection", "keep-alive");

		if (!request.hasBody()) {
			headers.add("Upgrade-Insecure-Requests", "1");
		}
		if (Objects.nonNull(request.getCacheControl())) {
			headers.add("Cache-Control", request.getCacheControl());
		}
		headers.add("Accept", request.getResponseEncType().getAcceptHeader());
		
		if (request.isXMLHttpRequest()) { //Sets the XML HTTP request headers
			headers.add("X-Requested-With", "XMLHttpRequest");
			headers.add("Origin", NetUtil.getBaseUrl(request.getUrl()));
		}
		headers.add("User-Agent", browser.getUserAgent());
		
		if (getBrowser().isDoNotTrack()) {
			headers.add("DNT", "1");
		}
        if (Objects.nonNull(lastGetUrl)) {
        	headers.add("Referer", lastGetUrl);
        }
        headers.add("Accept-Encoding", "gzip, deflate, br");
		headers.add("Accept-Language", browser.getLanguage());
		
		if (request.getRequestType() == RequestType.OPTIONS) { //Sets the request method for options request
			headers.add("Access-Control-Request-Method", ((OptionsRequest)request).getRequestTypeOption().getName()); //Sets the option request header
		}
		if (Objects.nonNull(request.getHeaders()) && !request.getHeaders().getHeaders().isEmpty()) { //Setting custom headers
			for (Entry<String, String> header : request.getHeaders().getHeaders().entrySet()) {
				if (Objects.isNull(header.getValue())) {
					headers.remove(header.getKey());
					continue;
				}
				headers.add(header.getKey(), header.getValue());
			}
		}
		if (request.getRequestType() != RequestType.OPTIONS
				&& !cookieJar.isEmpty() && !hasPolicy(ConnectionPolicy.DISABLE_COOKIES)) { //Setting the cookies
			StringBuilder cookieSb = new StringBuilder();
			
			for (int i = 0; i < cookieJar.getCookies().size(); i ++) {
				cookieSb.append(cookieJar.getCookies().get(i).getName() + "=" + cookieJar.getCookies().get(i).getValue());
				
				if (i < (cookieJar.getCookies().size() - 1)) {
					cookieSb.append("; ");
				}
			}
			headers.add("Cookie", cookieSb.toString());
		}
		if (request.hasBody()) {
			((ContentRequest)request).getBody().prepare(headers);
		}
		return headers;
	}

	/**
	 * Retrieves the connection security handler.
	 * 
	 * @return The connection security handler.
	 */
	public ConnectionSecurityHandler getConnectionSecurityHandler() {
		return conSecHandler;
	}
	
	/**
	 * Retrieves the proxy.
	 * 
	 * @return The proxy.
	 */
	public HttpProxy getProxy() {
		return proxy;
	}
	
	/**
	 * Retrieves the cookie jar.
	 * 
	 * @return The cookie jar.
	 */
	public CookieJar getCookieJar() {
		return cookieJar;
	}
	
	/**
	 * Retrieves the browser.
	 * 
	 * @return The browser.
	 */
	public Browser getBrowser() {
		return browser;
	}
	
	/**
	 * Retrieves the last url accessed through a GET request.
	 * 
	 * @return The url.
	 */
	public String getLastGetUrl() {
		return lastGetUrl;
	}

	/**
	 * Retrieves whether a given policy is present or not.
	 * 
	 * @param policy The policy.
	 * 
	 * @return the result.
	 */
	public boolean hasPolicy(ConnectionPolicy policy) {
		return policies.contains(policy);
	}
	
	/**
	 * Retrieves the connection policies.
	 * 
	 * @return The connection policies.
	 */
	public List<ConnectionPolicy> getPolicies() {
		return policies;
	}

}
