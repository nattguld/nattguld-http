package com.nattguld.http.proxies.rotating;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.ProxyAuthCredentials;
import com.nattguld.http.proxies.ProxyType;
import com.nattguld.http.proxies.cfg.LocalProxyConfig;
import com.nattguld.http.util.InternetConnectionType;

/**
 * 
 * @author randqm
 *
 */

public class RotatingProxy extends HttpProxy {
	
	/**
	 * The internet connection type.
	 */
	private final InternetConnectionType connType;
	
	/**
	 * The cooldown.
	 */
	private final int cooldown;
	
	/**
	 * Holds the user cooldowns.
	 */
	private final transient Map<String, Long> userCooldowns;
	
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 */
	public RotatingProxy(InternetConnectionType connType, int cooldown, String host, int port) {
		this(connType, cooldown, ProxyType.HTTP, host, port, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public RotatingProxy(InternetConnectionType connType, int cooldown, String host, int port, ProxyAuthCredentials authCreds) {
		this(connType, cooldown, ProxyType.HTTP, host, port, authCreds);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 */
	public RotatingProxy(InternetConnectionType connType, int cooldown, ProxyType proxyType, String host, int port) {
		 this(connType, cooldown, new LocalProxyConfig(), proxyType, host, port, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public RotatingProxy(InternetConnectionType connType, int cooldown, ProxyType proxyType, String host, int port, ProxyAuthCredentials authCreds) {
		 this(connType, cooldown, new LocalProxyConfig(), proxyType, host, port, authCreds);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 */
	public RotatingProxy(InternetConnectionType connType, int cooldown, LocalProxyConfig lCfg, String host, int port) {
		this(connType, cooldown, lCfg, ProxyType.HTTP, host, port, null);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param host The host address.
	 * 
	 * @param port The port.
	 * 
	 * @param authCreds The proxy authentication credentials.
	 */
	public RotatingProxy(InternetConnectionType connType, int cooldown, LocalProxyConfig lCfg, String host, int port, ProxyAuthCredentials authCreds) {
		this(connType, cooldown, lCfg, ProxyType.HTTP, host, port, authCreds);
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @param cooldown The proxy cooldown.
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
	public RotatingProxy(InternetConnectionType connType, int cooldown, LocalProxyConfig lCfg, ProxyType proxyType, String host, int port, ProxyAuthCredentials authCreds) {
		super(lCfg, proxyType, host, port, authCreds);
		
		this.connType = connType;
		this.cooldown = cooldown;
		this.userCooldowns = new ConcurrentHashMap<>();
	}
	
	/**
	 * Creates a new proxy.
	 * 
	 * @param reader The json reader.
	 */
	public RotatingProxy(JsonReader reader) {
		super(reader);
		
		this.connType = (InternetConnectionType)getReader().getAsObject("connection_type", InternetConnectionType.class);
		this.cooldown = getReader().getAsInt("cooldown");
		this.userCooldowns = new ConcurrentHashMap<>();
	}
	
	@Override
	public void write(JsonWriter writer) {
		super.write(writer);
		
		writer.write("connection_type", connType);
		writer.write("cooldown", cooldown);
	}
	
	@Override
	protected String getSaveDirName() {
		return "proxies" + File.separator + "rotating";
	}
	
	@Override
	protected String getSaveFileName() {
		return getUUID() + ".rp";
	}
	
	/**
	 * Retrieves whether the proxy is on cooldown for a given user or not.
	 * 
	 * @param user The user.
	 * 
	 * @return The result.
	 */
	public boolean isOnCooldown(String user) {
		if (cooldown <= 0) {
			return false;
		}
		if (userCooldowns.containsKey(user)) {
			if (System.currentTimeMillis() - userCooldowns.get(user) < getCooldownMs()) {
				return true;
			}
			userCooldowns.remove(user);
		}
		return false;
	}
	
	/**
	 * Indicates the proxy is in use by a given user.
	 * 
	 * @param user The user.
	 * 
	 * @return The proxy.
	 */
	public RotatingProxy setInUse(String user) {
		if (cooldown <= 0) {
			return this;
		}
		userCooldowns.put(user, System.currentTimeMillis());
		return this;
	}
	
	/**
	 * Retrieves the internet connection type.
	 * 
	 * @return The connection type.
	 */
	public InternetConnectionType getConnType() {
		return connType;
	}
	
	/**
	 * Retrieves the cooldown.
	 * 
	 * @return The cooldown.
	 */
	public int getCooldown() {
		return cooldown;
	}
	
	/**
	 * Retrieves the cooldown in milliseconds.
	 * 
	 * @return The cooldown.
	 */
	public long getCooldownMs() {
		return cooldown * 60 * 1000;
	}
	
	@Override
	public String toString() {
		return super.toString() + ":" + getCooldown();
	}

}
