package com.nattguld.http.proxies.cfg;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author randqm
 *
 */

public enum ProxyChoice {
	
	ROTATING_RESIDENTIAL("Rotating Residential", true),
	ROTATING_DATACENTER("Rotating Datacenter", true),
	IMPORTED("Imported", true),
	MANUAL("Manual", false),
	SCRAPED("Scraped", false),
	DIRECT("None/VPN/4G", true);
	
	
	/**
	 * The choice name.
	 */
	private final String name;
	
	/**
	 * Whether the choice is configurable or not.
	 */
	private boolean configurable;
	
	
	/**
	 * Creates a new proxy choice.
	 * 
	 * @param name The choice name.
	 * 
	 * @param configurable Whether the choice is configurable or not.
	 */
	private ProxyChoice(String name, boolean configurable) {
		this.name = name;
		this.configurable = configurable;
	}
	
	/**
	 * Retrieves the choice name.
	 * 
	 * @return The choice name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves whether the choice is configurable or not.
	 * 
	 * @return The result.
	 */
	public boolean isConfigurable() {
		return configurable;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * Retrieves the configurable choices.
	 * 
	 * @return The configurable choices.
	 */
	public static List<ProxyChoice> getConfigurables() {
		List<ProxyChoice> choices = new LinkedList<>();
		
		for (ProxyChoice pc : values()) {
			if (pc.isConfigurable()) {
				choices.add(pc);
			}
		}
		return choices;
	}
	
	/**
	 * Retrieves a choice based on a given identifier.
	 * 
	 * @param identifier The identifier.
	 * 
	 * @param defaultChoice The default choice if nothing found.
	 * 
	 * @return The choice.
	 */
	public static ProxyChoice get(String identifier, ProxyChoice defaultChoice) {
		for (ProxyChoice pc : values()) {
			if (pc.name().equalsIgnoreCase(identifier)
					|| pc.getName().equalsIgnoreCase(identifier)) {
				return pc;
			}
		}
		return defaultChoice;
	}

}
