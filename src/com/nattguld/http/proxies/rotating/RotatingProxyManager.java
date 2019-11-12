package com.nattguld.http.proxies.rotating;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.nattguld.data.json.JsonReader;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.ProxyManager;
import com.nattguld.http.proxies.ProxyType;
import com.nattguld.http.proxies.cfg.LocalProxyConfig;
import com.nattguld.http.proxies.cfg.ProxyConfig;
import com.nattguld.http.util.InternetConnectionType;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class RotatingProxyManager extends ProxyManager {

	/**
	 * The proxy manager instance.
	 */
	private static RotatingProxyManager singleton;
	
	
	@Override
	protected RotatingProxy instantiateResource(JsonReader reader) {
		return new RotatingProxy(reader);
	}

	@Override
	protected String getStorageDirName() {
		return "proxies" + File.separator + "rotating";
	}
	
	/**
	 * Retrieves the active proxy connections for a given connection type.
	 * 
	 * @param connType The connection type.
	 * 
	 * @return The active connections.
	 */
	public int getActiveConnections(InternetConnectionType connType) {
		int count = 0;
		
		for (RotatingProxy rp : getProxies(connType)) {
			count += rp.getLocalConfig().getConnectionsInUse();
		}
		return count;
	}
	
	/**
	 * Retrieves a random rotating proxy.
	 * 
	 * @return The proxy.
	 */
	public RotatingProxy getRandomProxy(InternetConnectionType connType) {
		return getRandomProxy(connType, null, false);
	}
	
	/**
	 * Retrieves a random rotating proxy.
	 * 
	 * @param user The user.
	 * 
	 * @param unique Whether the user should be unique for the proxy or not.
	 * 
	 * @return The proxy.
	 */
	public RotatingProxy getRandomProxy(InternetConnectionType connType, String user, boolean unique) {
		int maxThreads = (connType == InternetConnectionType.DATACENTER 
				? ProxyConfig.getConfig().getMaxRotatingDatacenterProxyThreads()
						: ProxyConfig.getConfig().getMaxRotatingResidentialProxyThreads());
		
		if (getActiveConnections(connType) > maxThreads) {
			return null;
		}
		List<RotatingProxy> available = getProxies(connType, user, unique);
		return available.isEmpty() ? null : available.get(Maths.random(available.size()));
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param connType The connection type.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public RotatingProxy parse(InternetConnectionType connType, String input) {
		return parse(connType, new LocalProxyConfig(), input);
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param connType The connection type.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public RotatingProxy parse(InternetConnectionType connType, LocalProxyConfig lCfg, String input) {
		return parse(connType, lCfg, ProxyType.HTTP, input);
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param connType The connection type.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public RotatingProxy parse(InternetConnectionType connType, ProxyType proxyType, String input) {
		return parse(connType, new LocalProxyConfig(), proxyType, input);
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param connType The connection type.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public RotatingProxy parse(InternetConnectionType connType, LocalProxyConfig lCfg, ProxyType proxyType, String input) {
		HttpProxy proxy = super.parseProxyString(lCfg, proxyType, input);
		
		if (Objects.isNull(proxy)) {
			return null;
		}
		String lastPart = input.substring(input.lastIndexOf(":") + 1, input.length());
		int cooldown = Maths.parseInt(lastPart, 0);
		return new RotatingProxy(connType, cooldown, proxy.getLocalConfig(), proxy.getType(), proxy.getHost(), proxy.getPort(), proxy.getAuthCreds());
	}
	
	//InternetConnectionType connType, int cooldown, LocalProxyConfig lCfg, ProxyType proxyType, String host, int port, ProxyAuthCredentials authCreds
	
	/**
	 * Retrieves the rotating proxies for a given internet connection type.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @return The proxies.
	 */
	public List<RotatingProxy> getProxies(InternetConnectionType connType) {
		return getProxies(connType, null, false);
	}
	
	/**
	 * Retrieves the rotating proxies for a given internet connection type.
	 * 
	 * @param connType The internet connection type.
	 * 
	 * @return The proxies.
	 */
	public List<RotatingProxy> getProxies(InternetConnectionType connType, String user, boolean unique) {
		List<RotatingProxy> proxies = new ArrayList<>();
		
		for (HttpProxy p : getResources()) {
			RotatingProxy rp = (RotatingProxy)p;
			
			if (rp.getConnType() != connType || !rp.getLocalConfig().canAddUser(user, unique)) {
				continue;
			}
			proxies.add(rp);
		}
		return proxies;
	}
	
	/**
	 * Retrieves the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static RotatingProxyManager getSingleton() {
		if (Objects.isNull(singleton)) {
			singleton = (RotatingProxyManager)new RotatingProxyManager().load();
		}
		return singleton;
	}

}
