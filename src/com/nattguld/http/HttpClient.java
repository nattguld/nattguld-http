package com.nattguld.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.cookies.CookieJar;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.rotating.RotatingProxy;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.execute.RequestBuilder;
import com.nattguld.http.requests.execute.RequestExecutor;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.http.response.bodies.impl.FileResponseBody;
import com.nattguld.http.sec.ConnectionSecurityHandler;
import com.nattguld.http.socket.HttpSocket;

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
	 * The request executor.
	 */
	private final RequestExecutor requestExecutor;
	
	/**
	 * The connection security handler if required.
	 */
	private ConnectionSecurityHandler conSecHandler;
	
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
		this.httpSocket = new HttpSocket();
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
		this.requestExecutor = new RequestExecutor(httpSocket, proxy, browser, cookieJar, dataCounter
				, hasPolicy(ConnectionPolicy.NO_AUTO_REDIRECT), hasPolicy(ConnectionPolicy.DISABLE_COOKIES));
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
	 * @param previous The previous requests in the chain.
	 * 
	 * @return The request response.
	 */
	public RequestResponse dispatchRequest(Request request) {
		RequestResponse rr = requestExecutor.execute(request);
		
		if (rr.validate(0)) {
			return rr;
		}
		if (!ConnectionSecurityHandler.bypass(this, request, rr)) {
			return dispatchRequest(request);
		}
		return rr;
	}
	
	
	public RequestResponse dispatchRequest(RequestBuilder rb) {
		RequestResponse rr = rb.execute(requestExecutor);
		
		if (rr.validate(0)) {
			return rr;
		}
		/*if (conSecHandler.hasImplementations()) {
			if (!conSecHandler.bypass(this, request, rr)) {
				return dispatchRequest(request);
			}
		}*/
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
		
		if (NetConfig.getConfig().isDebug()) {
			System.out.println("Downloaded " + f.getName() + " in " + ((System.currentTimeMillis() - start) / 1000) + "s");
		}
		return f;
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
	 * Retrieves the request executor.
	 * 
	 * @return The request executor.
	 */
	public RequestExecutor getRequestExecutor() {
		return requestExecutor;
	}
	
	/**
	 * Modifies the last referer url.
	 * 
	 * @param lastReferer The new last referer url.
	 * 
	 * @return The client.
	 */
	@Deprecated
	public HttpClient setLastReferer(String lastReferer) {
		getRequestExecutor().setLastReferer(lastReferer);
		return this;
	}
	
	/**
	 * Retrieves the last refered url.
	 * 
	 * @return The url.
	 */
	@Deprecated
	public String getLastReferer() {
		return getRequestExecutor().getLastReferer();
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
