package com.nattguld.http.proxies.rotating;

/**
 * 
 * @author randqm
 *
 */

public class ProxyUse {
	
	/**
	 * The user.
	 */
	private final String user;
	
	/**
	 * The last use of the proxy by the user.
	 */
	private long lastUse;
	
	/**
	 * Whether the proxy is actively used by the user or not.
	 */
	private boolean active;
	
	
	/**
	 * Creates a new proxy use.
	 * 
	 * @param user The user.
	 */
	public ProxyUse(String user) {
		this.user = user;
		
		updateLastUse();
	}
	
	/**
	 * Deactivates use.
	 */
	public void deactivate() {
		this.active = false;
	}
	
	/**
	 * Retrieves whether the proxy is actively used by the user or not.
	 * 
	 * @return The result.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Retrieves the user.
	 * 
	 * @return The user.
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Updates the last use time of the user.
	 */
	public void updateLastUse() {
		this.lastUse = System.currentTimeMillis();
		this.active = true;
	}
	
	/**
	 * Retrieves the last use.
	 * 
	 * @return The last use.
	 */
	public long getLastUse() {
		return lastUse;
	}

}
