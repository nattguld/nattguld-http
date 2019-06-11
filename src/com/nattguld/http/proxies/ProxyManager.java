package com.nattguld.http.proxies;

import java.util.Arrays;
import java.util.Objects;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.proxies.cfg.ProxyChoice;
import com.nattguld.http.proxies.rotating.ProxyGateway;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class ProxyManager {
	
	/**
	 * Represents an invalid proxy for error handling.
	 */
	public static final HttpProxy INVALID_PROXY = new HttpProxy("INVALID", 0);
	
	/**
	 * The fiddler proxy.
	 */
	public static final HttpProxy FIDDLER_PROXY = new HttpProxy("127.0.0.1", 8888);
	

	/**
	 * Retrieves a random imported proxy.
	 * 
	 * @return The random imported proxy.
	 */
	public static HttpProxy getRandom() {
		if (NetConfig.getGlobalInstance().getProxyConfigs().getImportedProxies().isEmpty()) {
			System.err.println("No proxies imported");
			return null;
		}
		return NetConfig.getGlobalInstance().getProxyConfigs().getImportedProxies().get(Maths.random(
				NetConfig.getGlobalInstance().getProxyConfigs().getImportedProxies().size()));
	}
	
	/**
	 * Retrieves an imported proxy by it's address.
	 * 
	 * @param address The address.
	 * 
	 * @return The proxy.
	 */
	public static HttpProxy getByAddress(String address) {
		return NetConfig.getGlobalInstance().getProxyConfigs().getImportedProxies().stream()
				.filter(p -> p.getAddress().equals(address))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Parses a string to a proxy.
	 * 
	 * @param input The input.
	 * 
	 * @return The proxy.
	 */
	public static HttpProxy parse(String input) {
		return parse(ProxyType.HTTP, input);
	}
	
	/**
	 * Parses a string to a proxy.
	 * 
	 * @param type The proxy type.
	 * 
	 * @param input The input.
	 * 
	 * @return The proxy.
	 */
	public static HttpProxy parse(ProxyType type, String input) {
		if (Objects.isNull(input)) {
			return null;
		}
		String trim = input.trim();
		
		if (input.isEmpty()) {
			return null;
		}
		String[] parts = trim.split(":");
		
		if (parts.length != 2 && parts.length != 4) {
			System.err.println("Invalid amount of parts (" + parts.length + ")");
			return null;
		}
		String host = parts[0];
		String[] hostParts = host.split("\\.");
		
		if (hostParts.length != 4 && hostParts.length != 3) {
			System.err.println("Invalid host address parts (" + host + ")");
			return null;
		}
		if (!Maths.isInteger(parts[1])) {
			System.err.println("Port is not an integer value");
			return null;
		}
		int port = Maths.parseInt(parts[1], -1);
		String username = null;
		String password = null;
		
		if (parts.length == 4) {
			username = parts[2];
			password = parts[3];
		}
		return new HttpProxy(type, host, port, username, password);
	}
	
	/**
	 * Retrieves a proxy based on proxy preferences.
	 * 
	 * @param proxyPrefs The proxy preferences.
	 * 
	 * @param user The user.
	 * 
	 * @param ignoreUsers Whether to ignore users or not.
	 * 
	 * @param ignoreCooldowns Whether to ignore cooldowns on proxies or not.
	 * 
	 * @return The proxy.
	 */
	public static HttpProxy getProxyByPreference(ProxyChoice[] choices, String user, boolean ignoreUsers, boolean ignoreCooldowns) {
		if (NetConfig.getGlobalInstance().getProxyConfigs().isFiddler()) {
    		return FIDDLER_PROXY;
    	}
		if (Objects.isNull(choices) || choices.length < 1) {
			System.err.println("No proxy choices passed");
			return null;
		}
		for (ProxyChoice choice : choices) {
			if (choice == ProxyChoice.ROTATING_RESIDENTIAL || choice == ProxyChoice.ROTATING_DATACENTER) {
				ProxyGateway pg = choice == ProxyChoice.ROTATING_RESIDENTIAL 
						? NetConfig.getGlobalInstance().getProxyConfigs().getResidentialGateway()
						: NetConfig.getGlobalInstance().getProxyConfigs().getDatacenterGateway();
				
				if (Objects.isNull(pg)) {
					continue;
				}
				return pg.getNext(user, ignoreUsers, ignoreCooldowns);
			}
			if (choice == ProxyChoice.IMPORTED) {
	    		HttpProxy p = getRandom();
	    		
	    		if (Objects.isNull(p)) {
	    			continue;
	    		}
	    		return p;
			}
			if (choice == ProxyChoice.SCRAPED) { //TODO
	    		/*HttpProxy p = getRandomScrapedProxy();
	    		
	    		if (Objects.isNull(p)) {
	    			continue;
	    		}
	    		return p;*/
				return null;
	    	}
	    	if (choice == ProxyChoice.DIRECT) {
	    		return null;
	    	}
    	}
		System.err.println("Failed to find proxy for choices: " + Arrays.toString(choices));
    	return INVALID_PROXY;
	}
	
	/**
	 * Retrieves the best available proxy choice.
	 * 
	 * @return The proxy choice.
	 */
	public static ProxyChoice findBestChoice() {
		if (Objects.nonNull(NetConfig.getGlobalInstance().getProxyConfigs().getResidentialGateway())) {
			return ProxyChoice.ROTATING_RESIDENTIAL;
		}
		if (!NetConfig.getGlobalInstance().getProxyConfigs().getImportedProxies().isEmpty()) {
			return ProxyChoice.IMPORTED;
		}
		if (Objects.nonNull(NetConfig.getGlobalInstance().getProxyConfigs().getDatacenterGateway())) {
			return ProxyChoice.ROTATING_DATACENTER;
		}
		return ProxyChoice.DIRECT;
	}

}
