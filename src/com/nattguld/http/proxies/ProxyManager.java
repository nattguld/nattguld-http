package com.nattguld.http.proxies;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.nattguld.data.json.JsonResourceManager;
import com.nattguld.http.proxies.cfg.LocalProxyConfig;
import com.nattguld.http.proxies.cfg.ProxyChoice;
import com.nattguld.http.proxies.cfg.ProxyConfig;
import com.nattguld.http.proxies.rotating.RotatingProxy;
import com.nattguld.http.proxies.rotating.RotatingProxyManager;
import com.nattguld.http.proxies.standard.StandardProxyManager;
import com.nattguld.http.util.InternetConnectionType;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public abstract class ProxyManager extends JsonResourceManager<HttpProxy> {
	
	/**
	 * Represents an invalid proxy for error handling.
	 */
	public static final HttpProxy INVALID_PROXY = new HttpProxy("INVALID", 0);
	
	/**
	 * The fiddler proxy.
	 */
	public static final HttpProxy FIDDLER_PROXY = new HttpProxy("127.0.0.1", 8888);

	
	static {
		ProxyConfig.getConfig();
	}
	
	/**
	 * Adds a proxy or retrieves it if it already exists.
	 * 
	 * @param proxy The proxy.
	 * 
	 * @return The proxy.
	 */
	public HttpProxy addOrRetrieve(HttpProxy proxy) {
		add(proxy);
		return getProxyByAddress(proxy.toString());
	}
	
	@Override
	public void add(HttpProxy proxy) {
		super.add(proxy, new Predicate<HttpProxy>() {
			@Override
			public boolean test(HttpProxy proxy) {
				return Objects.isNull(getProxyByAddress(proxy.toString()));
			}
		});
	}

	/**
	 * Retrieves a proxy by a given address.
	 * 
	 * @param address The address.
	 * 
	 * @return The proxy.
	 */
	public HttpProxy getProxyByAddress(String address) {
		return getResources().stream()
				.filter(p -> p.toString().equals(address))
				.findFirst().orElse(null);
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
	protected HttpProxy parseProxyString(LocalProxyConfig lCfg, ProxyType proxyType, String input) {
		if (Objects.isNull(input)) {
			System.err.println("[ProxyManager] Unable to parse proxy: nulled input");
			return null;
		}
		String trim = input.trim();
		
		if (trim.isEmpty()) {
			System.err.println("[ProxyManager] Unable to parse proxy: empty input");
			return null;
		}
		if (!trim.contains(":")) {
			System.err.println("[ProxyManager] Unable to parse proxy: invalid format");
			return null;
		}
		String[] parts = trim.split(":");
		
		if (parts.length < 2 || parts.length > 5) {
			System.err.println("[ProxyManager] Unable to parse proxy: invalid amount of parts (" + parts.length + ")");
			return null;
		}
		String host = parts[0].trim();

		if (host.split("\\.").length != 4) {
			System.err.println("[ProxyManager] Unable to parse proxy: invalid host");
			return null;
		}
		if (!Maths.isInteger(parts[1])) {
			System.err.println("[ProxyManager] Unable to parse proxy: invalid port");
			return null;
		}
		int port = Maths.parseInt(parts[1].trim(), -1);
		ProxyAuthCredentials authCreds = null;
		
		if (parts.length >= 4) {
			authCreds = new ProxyAuthCredentials(parts[2].trim(), parts[3].trim());
		}
		return new HttpProxy(lCfg, proxyType, host, port, authCreds);
	}
	
	/**
	 * Retrieves a proxy based on proxy preferences.
	 * 
	 * @param choices The proxy choices.
	 * 
	 * @param user The user.
	 * 
	 * @param unique Whether the user should be unique to the proxy or not.
	 * 
	 * @return The proxy.
	 */
	public static HttpProxy getProxyByChoices(ProxyChoice[] choices, String user, boolean unique) {
		if (ProxyConfig.getConfig().isFiddler()) {
    		return FIDDLER_PROXY;
    	}
		if (Objects.isNull(choices) || choices.length <= 0) {
			System.err.println("[ProxyManager]: No proxy choices passed");
			return INVALID_PROXY;
		}
		for (ProxyChoice choice : choices) {
			if (choice == ProxyChoice.DIRECT) {
				return null;
			}
			if (choice == ProxyChoice.IMPORTED) {
				HttpProxy proxy = StandardProxyManager.getSingleton().getRandomProxy(user, unique);
	    		
	    		if (Objects.isNull(proxy)) {
	    			continue;
	    		}
	    		return proxy;
			}
			if (choice == ProxyChoice.MANUAL) {
				System.err.println("[ProxyManager]: Manual is not a valid proxy choice");
				return INVALID_PROXY;
			}
			if (choice == ProxyChoice.SCRAPED) {
				System.err.println("[ProxyManager]: Scraped proxies are currently not supported");
				return INVALID_PROXY;
			}
			if (choice == ProxyChoice.ROTATING_DATACENTER || choice == ProxyChoice.ROTATING_RESIDENTIAL) {
				RotatingProxy rp = RotatingProxyManager.getSingleton().getRandomProxy(choice == ProxyChoice.ROTATING_DATACENTER ? InternetConnectionType.DATACENTER
								: InternetConnectionType.RESIDENTIAL, user, unique);
				
				if (Objects.isNull(rp)) {
	    			continue;
	    		}
				return rp;
			}
		}
		System.err.println("[ProxyManager]: Failed to find proxy for choices: " + Arrays.toString(choices));
    	return INVALID_PROXY;
	}
	
	/**
	 * Retrieves the best available proxy choice.
	 * 
	 * @return The proxy choice.
	 */
	public static ProxyChoice findBestChoice() {
		if (!RotatingProxyManager.getSingleton().getProxies(InternetConnectionType.RESIDENTIAL).isEmpty()) {
			return ProxyChoice.ROTATING_RESIDENTIAL;
		}
		if (!StandardProxyManager.getSingleton().getResources().isEmpty()) {
			return ProxyChoice.IMPORTED;
		}
		if (!RotatingProxyManager.getSingleton().getProxies(InternetConnectionType.DATACENTER).isEmpty()) {
			return ProxyChoice.ROTATING_DATACENTER;
		}
		return ProxyChoice.DIRECT;
	}
	
	/**
	 * Retrieves the proxies matching a given proxy type.
	 * 
	 * @param proxyType The proxy type.
	 * 
	 * @return The proxies matching the given proxy type.
	 */
	public List<HttpProxy> getByProxyType(ProxyType proxyType) {
		return getResources().stream()
				.filter(p -> p.getType() == proxyType)
				.collect(Collectors.toList());
	}

}
