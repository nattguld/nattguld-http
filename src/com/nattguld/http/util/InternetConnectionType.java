package com.nattguld.http.util;

/**
 * 
 * @author randqm
 *
 */

public enum InternetConnectionType {
	
	DATACENTER("Datacenter"),
	RESIDENTIAL("Residential"),
	CELLULAR("Cellular");
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	
	/**
	 * Creates a new internet connection type.
	 * 
	 * @param name The name.
	 */
	private InternetConnectionType(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
