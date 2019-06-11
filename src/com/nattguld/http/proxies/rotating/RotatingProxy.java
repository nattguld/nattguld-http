package com.nattguld.http.proxies.rotating;

import com.nattguld.http.proxies.HttpProxy;

/**
 * 
 * @author randqm
 *
 */

public class RotatingProxy extends HttpProxy {

	/**
	 * The cooldown.
	 */
	private final int cooldown;
	
	
	/**
	 * Creates a new rotating proxy.
	 * 
	 * @param host The host.
	 * 
	 * @param port The port.
	 */
	public RotatingProxy(String host, int port, int cooldown) {
		super(host, port);
		
		this.cooldown = cooldown;
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

}
