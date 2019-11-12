package com.nattguld.http.proxies.standard;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.nattguld.data.json.JsonReader;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.ProxyManager;
import com.nattguld.http.proxies.ProxyType;
import com.nattguld.http.proxies.cfg.LocalProxyConfig;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class StandardProxyManager extends ProxyManager {

	/**
	 * The proxy manager instance.
	 */
	private static StandardProxyManager singleton;
	
	
	@Override
	protected HttpProxy instantiateResource(JsonReader reader) {
		return new HttpProxy(reader);
	}

	@Override
	protected String getStorageDirName() {
		return "proxies" + File.separator + "standard";
	}
	
	/**
	 * Retrieves a random proxy.
	 * 
	 * @return The random proxy.
	 */
	public HttpProxy getRandomProxy() {
		return getRandomProxy(null, false);
	}
	
	/**
	 * Retrieves a random proxy.
	 * 
	 * @param user The user.
	 * 
	 * @param unique Whether the user should be unique for the proxy or not.
	 * 
	 * @return The proxy.
	 */
	public HttpProxy getRandomProxy(String user, boolean unique) {
		List<HttpProxy> available = getProxies(user, unique);
		return available.isEmpty() ? null : available.get(Maths.random(available.size()));
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public HttpProxy parse(String input) {
		return parse(new LocalProxyConfig(), input);
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public HttpProxy parse(LocalProxyConfig lCfg, String input) {
		return parse(lCfg, ProxyType.HTTP, input);
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public HttpProxy parse(ProxyType proxyType, String input) {
		return parse(new LocalProxyConfig(), proxyType, input);
	}
	
	/**
	 * Parses a proxy from a given input string.
	 * 
	 * @param lCfg The local proxy config.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @param input The input string.
	 * 
	 * @return The parsed proxy if successful.
	 */
	public HttpProxy parse(LocalProxyConfig lCfg, ProxyType proxyType, String input) {
		return super.parseProxyString(lCfg, proxyType, input);
	}
	
	/**
	 * Retrieves the proxies currently available for use.
	 * 
	 * @return The available proxies.
	 */
	public List<HttpProxy> getProxies() {
		return getProxies(null, false);
	}
	
	/**
	 * Retrieves the proxies currently available for use.
	 * 
	 * @param The user identifier.
	 * 
	 * @param unique Whether the user should be unique or not.
	 * 
	 * @return The available proxies.
	 */
	public List<HttpProxy> getProxies(String user, boolean unique) {
		return getResources().stream()
				.filter(p -> p.getLocalConfig().canAddUser(user, unique))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<HttpProxy> getResources() {
		return super.getResources();
	}
	
	/**
	 * Retrieves the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static StandardProxyManager getSingleton() {
		if (Objects.isNull(singleton)) {
			singleton = (StandardProxyManager)new StandardProxyManager().load();
		}
		return singleton;
	}

}
