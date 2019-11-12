package com.nattguld.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.net.ssl.SSLHandshakeException;

import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.cookies.CookieJar;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.pooling.SocketPool;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.cfg.ProxyConfig;
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
import com.nattguld.http.stream.CountInputStream;
import com.nattguld.http.stream.CountOutputStream;
import com.nattguld.http.util.NetUtil;
import com.nattguld.util.Misc;

/**
 * 
 * @author randqm
 *
 */

public class HttpClient implements AutoCloseable {
	
	/**
	 * The http socket.
	 */
	private final HttpSocket httpSocket;
	
	/**
	 * The data counter.
	 */
	private final DataCounter dataCounter;
	
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
	 * The last refered url.
	 */
	private String lastReferer;
	
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
		this.httpSocket = SocketPool.getSingleton().getElement();
		this.dataCounter = new DataCounter();
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
			((RotatingProxy)proxy).setInUse(user);
		}
		return this;
	}
	
	@Override
	public void close() {
		if (Objects.nonNull(proxy)) {
			proxy.getLocalConfig().removeUser(user);
		}
		SocketPool.getSingleton().release(httpSocket);
	}

	/**
	 * Attempts to dispatch a request.
	 * 
	 * @param request The request.
	 * 
	 * @return The request response.
	 */
	public RequestResponse dispatchRequest(Request request) {
		return dispatchRequest(request, "Initial", false);
	}
	
	/**
	 * Attempts to dispatch a request.
	 * 
	 * @param request The request.
	 * 
	 * @param lastError The last error.
	 * 
	 * @param ssl Whether to enforce SSL or not.
	 * 
	 * @return The request response.
	 */
	public RequestResponse dispatchRequest(Request request, String lastError, boolean ssl) {
		request.setAttempts(request.getAttempts() + 1);
		
		if (request.getAttempts() > browser.getConnectionAttempts()) {
			RequestResponse tooManyAttemptsResponse = new RequestResponse(request.getUrl(), new ResponseStatus(HTTPCode.UNKNOWN, "Too many failed attempts (" + lastError + ")")
					, new StringResponseBody("Failed to dispatch request (" + lastError + ") (attempts: " + (request.getAttempts()  - 1) + ")"), null);
			request.setAttempts(0);
			return tooManyAttemptsResponse;
		}
		StringBuilder raw = new StringBuilder();
		Headers headers = prepareHeaders(request);
		RequestResponse rr = null;
		
		String host = NetUtil.getDomain(request.getUrl());
		String endpoint = request.getUrl().substring(request.getUrl().indexOf(host) + host.length(), request.getUrl().length());

		if (NetConfig.getConfig().isDebug()) {
			System.out.println("Host: " + host + ", Endpoint: " + endpoint);
		}
		try (Socket socket = httpSocket.connect(proxy, host, request.getPort(), browser, ssl)) {
			try (BufferedOutputStream out = new BufferedOutputStream(new CountOutputStream(socket.getOutputStream(), request.getDataCounter()))) {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true) {
					@Override
					public void println(String s) {
						super.println(s);
						
						if (NetConfig.getConfig().isDebug()) {
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
			
				if (NetConfig.getConfig().isDebug()) {
					System.err.println(raw.toString());
				}
				if (request.hasBody()) { 
					ContentRequest contReq = ((ContentRequest)request);
					
					if (Objects.nonNull(contReq.getBody().getContent())) {
						contReq.getBody().write(out);
					}
				}
				out.flush();

				try (BufferedInputStream bis = new BufferedInputStream(new CountInputStream(socket.getInputStream(), request.getDataCounter()))) {
					HeaderDecoder hd = new HeaderDecoder();
					hd.decode(bis);

					ResponseStatus rs = hd.getResponseStatus();
					Headers responseHeaders = hd.getHeaders();
					
					if (Objects.isNull(rs)) {
						System.err.println("Failed to decode headers [" + request.getRequestType().getName() + " => " + request.getUrl() + "]");
						return dispatchRequest(request);
					}
					cookieJar.importCookies(hd.getCookies());
					
					IResponseBody<?> responseBody = rs.getHttpCode() != HTTPCode.NO_CONTENT 
							? ResponseBodyParser.parseResponseBody(request, responseHeaders, bis) : new StringResponseBody("");
					rr = new RequestResponse(request.getUrl(), rs, responseBody, responseHeaders);

				}
				writer.close();

				if (NetConfig.getConfig().isDebug()) {
					System.out.println("Request Data [Down: " + request.getDataCounter().getDown() + ", Up: " + request.getDataCounter().getUp());
					System.out.println("Session Data [Down: " + dataCounter.getDown() + ", Up: " + dataCounter.getUp());
				}
				if (ProxyConfig.getConfig().isCellularMode()) {
					NetConfig.getConfig().getCellularDataCounter().addUp(request.getDataCounter().getUp());
					NetConfig.getConfig().getCellularDataCounter().addDown(request.getDataCounter().getDown());
				}
				dataCounter.addUp(request.getDataCounter().getUp());
				dataCounter.addDown(request.getDataCounter().getDown());
			}
		} catch (UnknownHostException ex) {
			return handleRequestException("Unknown host", ex, host, request, ssl);
			
		} catch (ConnectException ex) {
			return handleRequestException("Connection refused", ex, host, request, ssl);
			
		} catch (SocketException ex) {
			return handleRequestException("Socket exception", ex, host, request, ssl);
			
		} catch (SocketTimeoutException ex) {
			return handleRequestException("Timed out", ex, host, request, ssl);
			
		} catch (SSLHandshakeException ex) {
			return handleRequestException("SSL handshake exception", ex, host, request, ssl);
			
		} catch (IOException ex) {
			return handleRequestException("IO exception", ex, host, request, ssl);
			
		} catch (Exception ex) {
			return handleRequestException("Exception", ex, host, request, ssl);
			
		}
		if (rr.getCode() != request.getCode()) {
			if (rr.getResponseStatus().getHttpCode().isRedirection()) {
				if (!hasPolicy(ConnectionPolicy.NO_AUTO_REDIRECT)) {
					if (redirects >= 6) {
						redirects = 0;
						request.setAttempts(0);
						return new RequestResponse(request.getUrl(), new ResponseStatus(HTTPCode.UNKNOWN, "Too many redirects")
								, new StringResponseBody("Too many redirects through " + request.getUrl()), null);
					}
					String redirectUrl = rr.getLocation();
						
					if (Objects.nonNull(redirectUrl)) {
						if (!redirectUrl.startsWith("http")) {
							/*if (Objects.isNull(lastReferer)) {
								redirects = 0;
								request.setAttempts(0);
								return new RequestResponse(redirectUrl, new ResponseStatus(HTTPCode.UNKNOWN, "Failed to redirect")
										, new StringResponseBody("Failed to auto redirect for " + request.getUrl()), null);
							}*/
							redirectUrl = NetUtil.getBaseUrl(request.getUrl()) + redirectUrl;
						}
						if (NetConfig.getConfig().isDebug()) {
							System.out.println("Redirect (" + rr.getCode() + ") => " + redirectUrl + " [Last GET: " + lastReferer + "][Location: " + rr.getLocation() + "]");	
						}
						if (rr.getCode() == 301 && redirectUrl.equals(request.getUrl())) {
							redirects++;
							request.setAttempts(0);
							return dispatchRequest(request, "SSL redirect requested", true);
						}
						redirects++;
						request.setAttempts(0);
						Request redirReq = new GetRequest(redirectUrl, 200, request.getHeaders()).setPort(request.getPort())
								.setDecodeBody(request.isDecodeBody());
						redirReq.setSavePath(request.getSavePath());
						redirReq.setProgressListener(request.getProgressListener());
						return dispatchRequest(redirReq);
					}
					System.err.println("No redirect URL found on redirect response [" + request.getRequestType().getName() + " => " + request.getUrl() + "]");
				}
			} else if (rr.getResponseStatus().getHttpCode().isClientError()) {
				if (rr.getResponseStatus().getHttpCode() == HTTPCode.BAD_REQUEST) {
					if (!ssl && request.getUrl().startsWith("https")) {
						redirects++;
						request.setAttempts(0);
						return dispatchRequest(request, "Bad request, trying with SSL", true);
					}
				}
				if (rr.getResponseStatus().getHttpCode() == HTTPCode.TOO_MANY_REQUESTS) {
					System.err.println("Too many requests, waiting for 30 seconds to try again");
					Misc.sleep(30000);
					return dispatchRequest(request);
				}
				System.err.println("Client error " + rr.getResponseStatus().getHttpCode().toString());
				
			} else if (rr.getResponseStatus().getHttpCode().isServerError()) {
				System.err.println("Server error " + rr.getResponseStatus().getHttpCode().toString());
				
			} else if (!rr.getResponseStatus().getHttpCode().isSuccess()) {
				System.err.println("Unsuccessful request " + rr.getResponseStatus().getHttpCode().toString());
			}
		}
		redirects = 0;

		if (request.getRequestType() == RequestType.GET && !((GetRequest)request).isNoRef() && !request.isXMLHttpRequest()) {
			lastReferer = request.getUrl();
		}
		if (request instanceof ContentRequest) {
			ContentRequest contReq = ((ContentRequest)request);
			
			if (contReq.hasBody() && contReq.getBody().isChunked()) {
				if (!contReq.getBody().getChunkHandler().isFinished()) {
					if (Objects.nonNull(contReq.getProgressListener())) {
						contReq.getProgressListener().setProgress(contReq.getBody().getChunkHandler().getProgress());
					}
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
	 * Handles a request exception.
	 * 
	 * @param message The exception error message.
	 * 
	 * @param ex The exception.
	 * 
	 * @param host The host.
	 * 
	 * @param request The request.
	 * 
	 * @param ssl Whether to enforce SSL or not.
	 * 
	 * @return The new request response.
	 */
	private RequestResponse handleRequestException(String message, Exception ex, String host, Request request, boolean ssl) {
		if (NetConfig.getConfig().isDebug()) {
			ex.printStackTrace();
		}
		System.err.println(message + " (" + host + ") [" + request.getRequestType().getName() + " => " + request.getUrl() + "]");
		return dispatchRequest(request, message + " (" + host + ") [" + request.getRequestType().getName() + " => " + request.getUrl() + "]", ssl);
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
		
		if (NetConfig.getConfig().isDebug()) {
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
        if (Objects.nonNull(lastReferer)) {
        	headers.add("Referer", lastReferer);
        }
        headers.add("Accept-Encoding", "gzip, deflate, br");
		headers.add("Accept-Language", browser.getLanguage());
		
		if (request.getRequestType() == RequestType.OPTIONS) { //Sets the request method for options request
			headers.add("Access-Control-Request-Method", ((OptionsRequest)request).getRequestTypeOption().getName()); //Sets the option request header
			headers.add("Origin", NetUtil.getBaseUrl(request.getUrl()));
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
	 * Fetches the IP of the client session.
	 * 
	 * @return The IP.
	 */
	public String fetchIP() {
		try {
			RequestResponse rr = dispatchRequest(new GetRequest("https://api.ipify.org/").setNoRef(true));
		
			if (!rr.validate()) {
				return "Failed to fetch IP";
			}
			return rr.getResponseContent();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Failed to fetch IP";
		}
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
	 * Modifies the last referer url.
	 * 
	 * @param lastReferer The new last referer url.
	 * 
	 * @return The client.
	 */
	public HttpClient setLastReferer(String lastReferer) {
		this.lastReferer = lastReferer;
		return this;
	}
	
	/**
	 * Retrieves the last refered url.
	 * 
	 * @return The url.
	 */
	public String getLastReferer() {
		return lastReferer;
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
