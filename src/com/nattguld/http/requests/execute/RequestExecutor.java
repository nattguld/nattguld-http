package com.nattguld.http.requests.execute;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.net.ssl.SSLHandshakeException;

import com.nattguld.http.DataCounter;
import com.nattguld.http.HTTPCode;
import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.cookies.CookieJar;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.cfg.ProxyConfig;
import com.nattguld.http.requests.ContentRequest;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;
import com.nattguld.http.requests.headers.impl.DefaultHeadersBuilder;
import com.nattguld.http.requests.headers.impl.SecFetchHeadersBuilder;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.http.response.ResponseStatus;
import com.nattguld.http.response.bodies.IResponseBody;
import com.nattguld.http.response.bodies.ResponseBodyParser;
import com.nattguld.http.response.bodies.impl.StringResponseBody;
import com.nattguld.http.response.decode.impl.HeaderDecoder;
import com.nattguld.http.socket.HttpSocket;
import com.nattguld.http.socket.ITestSocket;
import com.nattguld.http.stream.CountInputStream;
import com.nattguld.http.stream.CountOutputStream;
import com.nattguld.http.util.NetUtil;
import com.nattguld.util.Misc;

/**
 * 
 * @author randqm
 *
 */

public class RequestExecutor {
	
	/**
	 * The maxiumum amount of redirects allowed in one chain.
	 */
	private static final int MAX_REDIRECTS = 5;
	
	/**
	 * The HTTP socket being used.
	 */
	private final HttpSocket httpSocket;
	
	/**
	 * The proxy being used.
	 */
	private final HttpProxy httpProxy;
	
	/**
	 * The browser being emulated.
	 */
	private final Browser browser;
	
	/**
	 * The cookie jar being used.
	 */
	private final CookieJar cookieJar;
	
	/**
	 * The data counter instance.
	 */
	private final DataCounter dataCounter;
	
	/**
	 * Whether auto redirection is disabled or not.
	 */
	private final boolean autoRedirectDisabled;
	
	/**
	 * Whether to disable cookies or not.
	 */
	private final boolean disableCookies;
	
	/**
	 * The redirection chain.
	 */
	private final List<Request> redirectionChain;
	
	/**
	 * The hosts that have been accessed by the client.
	 */
	private final List<String> accessedHosts;
	
	/**
	 * The last ocurred error.
	 */
	private String lastError;
	
	/**
	 * The last contacted host.
	 */
	private String lastContactedHost;
	
	/**
	 * The last refered url.
	 */
	private String lastReferer;

	
	/**
	 * Creates a new request executor.
	 * 
	 * @param httpSocket The HTTP socket being used.
	 * 
	 * @param httpProxy The proxy being used.
	 * 
	 * @param browser The browser being emulated.
	 * 
	 * @param cookieJar The cookie jar being used.
	 * 
	 * @param dataCounter The data counter instance.
	 * 
	 * @param autoRedirectDisabled Whether auto redirection is disabled or not.
	 * 
	 *  @param disableCookies Whether to disable cookies or not.
	 */
	public RequestExecutor(HttpSocket httpSocket, HttpProxy httpProxy, Browser browser, CookieJar cookieJar, DataCounter dataCounter
			, boolean autoRedirectDisabled, boolean disableCookies) {
		this.httpSocket = httpSocket;
		this.httpProxy = httpProxy;
		this.browser = browser;
		this.cookieJar = cookieJar;
		this.dataCounter = dataCounter;
		this.autoRedirectDisabled = autoRedirectDisabled;
		this.disableCookies = disableCookies;
		this.redirectionChain = new ArrayList<>();
		this.accessedHosts = new ArrayList<>();
		this.lastError = "Initial";
	}
	
	/**
	 * Executes a request.
	 * 
	 * @param request The request.
	 * 
	 * @return The request response.
	 */
	public RequestResponse execute(Request request) {
		String host = NetUtil.getDomain(request.getUrl());
		return execute(request, host, HttpSocket.SSL_HOSTS.contains(host));
	}
	
	/**
	 * Executes a request.
	 * 
	 * @param request The request.
	 * 
	 * @param host The host.
	 * 
	 * @param ssl Whether to use SSL or not.
	 * 
	 * @return The request response.
	 */
	protected RequestResponse execute(Request request, String host, boolean ssl) {
		request.setAttempts(request.getAttempts() + 1);
		
		if (request.getAttempts() > browser.getConnectionAttempts()) {
			RequestResponse tooManyAttemptsResponse = new RequestResponse(request.getUrl(), request.getCode()
					, new ResponseStatus(HTTPCode.UNKNOWN, "Too many failed attempts (" + lastError + ")")
					, new StringResponseBody("Failed to dispatch request (" + lastError + ") (attempts: " + (request.getAttempts()  - 1) + ")"), null);
			request.setAttempts(0);
			return tooManyAttemptsResponse;
		}
		//String host = NetUtil.getDomain(request.getUrl());
		String endpoint = request.getUrl().substring(request.getUrl().indexOf(host) + host.length(), request.getUrl().length());
		
		if (NetConfig.getConfig().isDebug()) {
			System.out.println("Host: " + host + ", Endpoint: " + endpoint);
		}
		Headers headers = new Headers();
		
		new DefaultHeadersBuilder(browser.getUserAgent(), browser.getLanguage(), lastReferer
				, browser.isDoNotTrack(), !redirectionChain.isEmpty(), disableCookies ? null : cookieJar)
			.build(request, headers);
		
		if (ssl) {
			new SecFetchHeadersBuilder(lastContactedHost, accessedHosts, redirectionChain).build(request, headers);
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
		StringBuilder raw = new StringBuilder();
		RequestResponse rr = null;

		try (ITestSocket socket = httpSocket.connect(httpProxy, host, request.getPort(), browser, ssl)) {
			try (BufferedOutputStream out = new BufferedOutputStream(new CountOutputStream(socket.getSocket().getOutputStream(), request.getDataCounter()))) {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true) {
					
					/**
					 * The string builder.
					 */
					private final StringBuilder raw = new StringBuilder();
					
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

				try (BufferedInputStream bis = new BufferedInputStream(new CountInputStream(socket.getSocket().getInputStream(), request.getDataCounter()))) {
					HeaderDecoder hd = new HeaderDecoder();
					hd.decode(bis);

					ResponseStatus rs = hd.getResponseStatus();
					Headers responseHeaders = hd.getHeaders();
					
					if (Objects.isNull(rs)) {
						System.err.println("Failed to decode headers [" + request.getRequestType().getName() + " => " + request.getUrl() + "]");
						return execute(request, host, ssl);
					}
					cookieJar.importCookies(hd.getCookies());
					
					IResponseBody<?> responseBody = rs.getHttpCode() != HTTPCode.NO_CONTENT 
							? ResponseBodyParser.parseResponseBody(request, responseHeaders, bis) : new StringResponseBody("");
					rr = new RequestResponse(request.getUrl(), request.getCode(), rs, responseBody, responseHeaders);

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
			socket.close();
			
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
		if (!accessedHosts.contains(host)) {
			accessedHosts.add(host);
		}
		lastContactedHost = host;
		
		if (rr.getCode() != request.getCode()) {
			if (rr.getResponseStatus().getHttpCode().isRedirection()) {
				if (!autoRedirectDisabled) {
					if (redirectionChain.size() >= MAX_REDIRECTS) {
						redirectionChain.clear();
						request.setAttempts(0);
						return new RequestResponse(request.getUrl(), request.getCode(), new ResponseStatus(HTTPCode.UNKNOWN, "Too many redirects")
								, new StringResponseBody("Too many redirects through " + request.getUrl()), null);
					}
					String redirectUrl = rr.getLocation();
						
					if (Objects.nonNull(redirectUrl)) {
						boolean externalHost = !redirectUrl.contains(host);
						
						if (!redirectUrl.startsWith("http")) {
							/*if (Objects.isNull(lastReferer)) {
								redirects = 0;
								request.setAttempts(0);
								return new RequestResponse(redirectUrl, new ResponseStatus(HTTPCode.UNKNOWN, "Failed to redirect")
										, new StringResponseBody("Failed to auto redirect for " + request.getUrl()), null);
							}*/
							redirectUrl = NetUtil.getBaseUrl(request.getUrl()) + redirectUrl;
							externalHost = false;
						}
						if (NetConfig.getConfig().isDebug()) {
							System.out.println("Redirect (" + rr.getCode() + ") => " + redirectUrl + " [Last GET: " + lastReferer + "][Location: " + rr.getLocation() + "]");	
						}
						redirectionChain.add(request);
						
						if (rr.getCode() == 301 && redirectUrl.equals(request.getUrl())) {
							request.setAttempts(0);
							lastError = "SSL redirect requested";
							System.err.println("SSL redirect");
							return execute(request, host, true);
						}
						request.setAttempts(0);
						Request redirReq = new GetRequest(redirectUrl, 200, request.getHeaders())
								.setPort(request.getPort()).setDecodeBody(request.isDecodeBody());
						redirReq.setSavePath(request.getSavePath());
						redirReq.setProgressListener(request.getProgressListener());
						return externalHost ? execute(redirReq) : execute(redirReq, host, ssl);
					}
					System.err.println("No redirect URL found on redirect response [" + request.getRequestType().getName() + " => " + request.getUrl() + "]");
				}
			} else if (rr.getResponseStatus().getHttpCode().isClientError()) {
				if (rr.getResponseStatus().getHttpCode() == HTTPCode.FORBIDDEN && !ssl) {
					request.setAttempts(0);
					lastError = "Forbidden, trying with SSL";
					return execute(request, host, true);
				}
				if (rr.getResponseStatus().getHttpCode() == HTTPCode.BAD_REQUEST) {
					if (!ssl && request.getUrl().startsWith("https")) {
						redirectionChain.add(request);
						request.setAttempts(0);
						lastError = "Bad request, trying with SSL";
						return execute(request, host, true);
					}
				}
				if (rr.getResponseStatus().getHttpCode() == HTTPCode.TOO_MANY_REQUESTS) {
					System.err.println("Too many requests, waiting for 30 seconds to try again");
					Misc.sleep(30000);
					lastError = "Too many requests";
					return execute(request, host, ssl);
				}
				System.err.println("Client error " + rr.getResponseStatus().getHttpCode().toString());
				
			} else if (rr.getResponseStatus().getHttpCode().isServerError()) {
				System.err.println("Server error " + rr.getResponseStatus().getHttpCode().toString());
				
			} else if (!rr.getResponseStatus().getHttpCode().isSuccess()) {
				System.err.println("Unsuccessful request " + rr.getResponseStatus().getHttpCode().toString());
			}
		}
		if (request.getRequestType() == RequestType.GET && !((GetRequest)request).isNoRef() 
				&& !request.isXMLHttpRequest() && request.getUrl().contains(host)) {
			lastReferer = request.getUrl();
		}
		redirectionChain.clear();

		if (request instanceof ContentRequest) {
			ContentRequest contReq = ((ContentRequest)request);
			
			if (contReq.hasBody() && contReq.getBody().isChunked()) {
				if (!contReq.getBody().getChunkHandler().isFinished()) {
					if (Objects.nonNull(contReq.getProgressListener())) {
						contReq.getProgressListener().setProgress(contReq.getBody().getChunkHandler().getProgress());
					}
					request.setAttempts(0);
					return execute(request, host, ssl);
				}
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
		//if (NetConfig.getConfig().isDebug()) {
			ex.printStackTrace();
		//}
		lastError = message + " (" + host + ") [" + request.getRequestType().getName() + " => " + request.getUrl() + "]";
		System.err.println(lastError);
		return execute(request, host, ssl);
	}
	
	/**
	 * Modifies the last referer.
	 * 
	 * @param lastReferer The new last referer.
	 * 
	 * @return The request executor.
	 */
	public RequestExecutor setLastReferer(String lastReferer) {
		this.lastReferer = lastReferer;
		return this;
	}
	
	/**
	 * Retrieves the last referer.
	 * 
	 * @return The last referer.
	 */
	public String getLastReferer() {
		return lastReferer;
	}

}
