package com.nattguld.http.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author randqm
 *
 */

public class StandardSocket implements ITestSocket {

	/**
	 * The socket.
	 */
	private final Socket socket;
	
	
	/**
	 * Creates a new standard socket.
	 * 
	 * @param socket The socket.
	 */
	public StandardSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void close() throws IOException {
		if (!socket.isClosed()) {
			socket.close();
		}
	}

	@Override
	public Socket getSocket() {
		return socket;
	}

}
