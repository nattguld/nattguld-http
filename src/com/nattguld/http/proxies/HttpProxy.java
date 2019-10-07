package com.nattguld.http.proxies;

import java.net.InetSocketAddress;
import java.util.Objects;

import com.nattguld.util.hashing.Hasher;

/**
 * 
 * @author randqm
 *
 */

public class HttpProxy {
	
	/**
	 * The proxy type.
	 */
	private final ProxyType type;
	
	/**
	 * The host.
	 */
	private final String host;
	
	/**
	 * The port.
	 */
	private final int port;
	
	/**
	 * The authentication username.
	 */
	private final String username;
	
	/**
	 * The authentication password.
	 */
	private final String password;
	
	/**
	 * The base 64 auth.
	 */
	private String base64Auth;

	
	/**
	 * Creates a new proxy.
	 * 
	 * @param type The proxy type.
	 * 
	 * @param host host The host.
	 * 
	 * @param port The port.
	 * 
	 * @param username The authentication username.
	 * 
	 * @param password The authentication password.
	 */
	public HttpProxy(String host, int port, String username, String password) {
		this(ProxyType.HTTP, host, port, username, password);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param host The host.
	 * 
	 * @param port The port.
	 * 
	 * @param source The proxy source.
	 */
	public HttpProxy(String host, int port) {
		this(ProxyType.HTTP, host, port);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param type The proxy type.
	 * 
	 * @param host host The host.
	 * 
	 * @param port The port.
	 * 
	 * @param source The proxy source.
	 */
	public HttpProxy(ProxyType type, String host, int port) {
		this(type, host, port, null, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param type type The type.
	 * 
	 * @param host host The host.
	 * 
	 * @param port The port.
	 * 
	 * @param username The authentication username.
	 * 
	 * @param password The authentication password.
	 */
	public HttpProxy(ProxyType type, String host, int port, String username, String password) {
		this.type = type;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Retrieves the proxy type.
	 * 
	 * @return The type.
	 */
	public ProxyType getType() {
		return type;
	}
	
	/**
	 * Retrieves the host.
	 * 
	 * @return The host.
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Retrieves the port.
	 * 
	 * @return The port.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Retrieves the address.
	 * 
	 * @return The address.
	 */
	public String getAddress() {
		return getHost() + ":" + getPort();
	}
	
	/**
	 * Retrieves the authentication username.
	 * 
	 * @return The authentication username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Retrieves the authentication password.
	 * 
	 * @return The authentication password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Retrieves whether the proxy has authentication or not.
	 * 
	 * @return The result.
	 */
	public boolean hasAuthentication() {
		return Objects.nonNull(getUsername()) && Objects.nonNull(getPassword());
	}
	
	/**
	 * Retrieves the base64 authentication.
	 * 
	 * @return The base64 authentication.
	 */
	public String getBase64Auth() {
		if (Objects.isNull(base64Auth)) {
			if (!hasAuthentication()) {
				return null;
			}
			this.base64Auth = Hasher.base64(username + ":" + password);
		}
		return base64Auth;
	}
	
	/**
	 * Retrieves a java.net proxy version of the proxy.
	 * 
	 * @return The java.net Proxy.
	 */
	public java.net.Proxy toJavaProxy() {
		return new java.net.Proxy((getType() == ProxyType.HTTP || getType() == ProxyType.HTTPS) ? java.net.Proxy.Type.HTTP : java.net.Proxy.Type.SOCKS
				, new InetSocketAddress(getHost(), getPort()));
	}
	
	public String getAuthAddress() {
		return getAddress() + (hasAuthentication() ? (":" + getUsername() + ":" + getPassword()) : "");
	}
	
	@Override
	public String toString() {
		return getAuthAddress();
	}

}
