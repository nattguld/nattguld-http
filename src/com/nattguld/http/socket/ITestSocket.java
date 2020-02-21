package com.nattguld.http.socket;

import java.net.Socket;

/**
 * 
 * @author randqm
 *
 */

public interface ITestSocket extends AutoCloseable {
	
	
	/**
	 * Retrieves the socket to use.
	 * 
	 * @return The socket.
	 */
	public Socket getSocket();

}
