package com.nattguld.http.proxies;

import java.io.File;
import java.util.Objects;

import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonResource;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.http.proxies.cfg.LocalProxyConfig;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class HttpProxy extends JsonResource {
	
	/**
	 * The UUID.
	 */
	private final String uuid;
	
	/**
	 * The local proxy config.
	 */
	private final LocalProxyConfig lCfg;
	
	/**
	 * The proxy type.
	 */
	private final ProxyType proxyType;
	
	/**
	 * The host address.
	 */
	private final String host;
	
	/**
	 * The port.
	 */
	private final int port;
	
	/**
	 * The proxy authentication credentials.
	 */
	private final ProxyAuthCredentials authCreds;
	
	/**
	 * The proxy state.
	 */
	private ProxyState state;
	
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 */
	public HttpProxy(String host, int port) {
		this(ProxyType.HTTP, host, port, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public HttpProxy(String host, int port, ProxyAuthCredentials authCreds) {
		this(ProxyType.HTTP, host, port, authCreds);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 */
	public HttpProxy(ProxyType proxyType, String host, int port) {
		 this(new LocalProxyConfig(), proxyType, host, port, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public HttpProxy(ProxyType proxyType, String host, int port, ProxyAuthCredentials authCreds) {
		 this(new LocalProxyConfig(), proxyType, host, port, authCreds);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 */
	public HttpProxy(LocalProxyConfig lCfg, String host, int port) {
		this(lCfg, ProxyType.HTTP, host, port, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public HttpProxy(LocalProxyConfig lCfg, String host, int port, ProxyAuthCredentials authCreds) {
		this(lCfg, ProxyType.HTTP, host, port, authCreds);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public HttpProxy(LocalProxyConfig lCfg, ProxyType proxyType, String host, int port, ProxyAuthCredentials authCreds) {
		this.uuid = Maths.getUniqueId();
		this.lCfg = lCfg;
		this.proxyType = proxyType;
		this.host = host;
		this.port = port;
		this.authCreds = authCreds;
		this.state = ProxyState.ONLINE;
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param reader The json reader.
	 */
	public HttpProxy(JsonReader reader) {
		super(reader);
		
		this.uuid = getReader().getAsString("uuid", Maths.getUniqueId());
		this.lCfg = (LocalProxyConfig)getReader().getAsObject("local_config", LocalProxyConfig.class, new LocalProxyConfig());
		this.proxyType = (ProxyType)getReader().getAsObject("type", ProxyType.class, ProxyType.HTTP);
		this.host = getReader().getAsString("host");
		this.port = getReader().getAsInt("port");
		this.authCreds = (ProxyAuthCredentials)getReader().getAsObject("auth_credentials", ProxyAuthCredentials.class, null);
		this.state = (ProxyState)getReader().getAsObject("state", ProxyState.class, ProxyState.ONLINE);
	}
	
	@Override
	public void write(JsonWriter writer) {
		writer.write("uuid", uuid);
		writer.write("local_config", lCfg);
		writer.write("type", proxyType);
		writer.write("host", host);
		writer.write("port", port);
		writer.write("auth_credentials", authCreds);
		writer.write("state", state);
	}
	
	@Override
	protected String getSaveDirName() {
		return "proxies" + File.separator + "standard";
	}

	@Override
	protected String getSaveFileName() {
		return getUUID() + ".sp";
	}
	
	@Override
	public String getUUID() {
		return uuid;
	}
	
	/**
	 * Modifies the proxy state.
	 * 
	 * @param state The new proxy state.
	 * 
	 * @return The proxy.
	 */
	public HttpProxy setState(ProxyState state) {
		this.state = state;
		return this;
	}
	
	/**
	 * Retrieves the proxy state.
	 * 
	 * @return The proxy state.
	 */
	public ProxyState getState() {
		return state;
	}
	
	/**
	 * Retrieves the local config.
	 * 
	 * @return The local config.
	 */
	public LocalProxyConfig getLocalConfig() {
		return lCfg;
	}
	
	/**
	 * Retrieves the proxy type.
	 * 
	 * @return The proxy type.
	 */
	public ProxyType getType() {
		return proxyType;
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
	 * Retrieves the url.
	 * 
	 * @return The url.
	 */
	public String getUrl() {
		return getHost() + ":" + getPort();
	}
	
	/**
	 * Retrieves the proxy authentication credentials.
	 * 
	 * @return The authentication credentials.
	 */
	public ProxyAuthCredentials getAuthCreds() {
		return authCreds;
	}
	
	/**
	 * Retrieves whether the proxy uses authentication or not.
	 * 
	 * @return The result.
	 */
	public boolean hasAuthentication() {
		return Objects.nonNull(getAuthCreds());
	}
	
	@Override
	public boolean equals(Object other) {
		return Objects.nonNull(other) && other instanceof HttpProxy && ((HttpProxy)other).getUUID().equals(getUUID());
	}
	
	@Override
	public String toString() {
		return getUrl() + (hasAuthentication() ? (":" + getAuthCreds().toString()) : "");
	}

}
