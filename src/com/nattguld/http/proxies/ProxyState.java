package com.nattguld.http.proxies;

/**
 * 
 * @author randqm
 *
 */

public enum ProxyState {
	
	DOWN("Down"),
	ONLINE("Online"),
	GHOSTED("Ghosted"),
	BLACKLISTED("Blacklisted"),
	ISSUES("Issues");
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	
	/**
	 * Creates a new proxy state.
	 * 
	 * @param name The name.
	 */
	private ProxyState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
