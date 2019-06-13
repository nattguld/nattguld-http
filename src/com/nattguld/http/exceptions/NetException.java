package com.nattguld.http.exceptions;

import java.io.IOException;

import com.nattguld.http.cfg.NetConfig;

/**
 * 
 * @author randqm
 *
 */

@SuppressWarnings("serial")
public class NetException extends IOException {

	
	/**
	 * Creates a new network exception.
	 * 
	 * @param message The message.
	 */
	public NetException(String message) {
		super(message);
		
		if (NetConfig.getConfig().isDeveloperMode()) {
			NetConfig.getConfig().setDebug(true);
		}
	}
	
	/**
	 * Creates a new network exception.
	 * 
	 * @param ex The exception.
	 */
	public NetException(Exception ex) {
		super(ex);
		
		if (NetConfig.getConfig().isDeveloperMode()) {
			NetConfig.getConfig().setDebug(true);
		}
	}
	
}
