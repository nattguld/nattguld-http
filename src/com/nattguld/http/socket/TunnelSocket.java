package com.nattguld.http.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author randqm
 *
 */

public class TunnelSocket implements ITestSocket {
	
	/**
	 * The main Socket.
	 */
	private final Socket socket;
	
	/**
	 * The socket tunnel.
	 */
	private final Socket tunnel;
	
	
	/**
	 * Creates a new tunnel SSL socket.
	 * 
	 * @param sslSocket The SSL Socket.
	 * 
	 * @param tunnel The socket tunnel.
	 */
	public TunnelSocket(Socket sslSocket, Socket tunnel) {
		this.socket = sslSocket;
		this.tunnel = tunnel;
	}
	
	@Override
	public void close() throws IOException {
		if (!socket.isClosed()) {
			socket.close();
		}
		if (!tunnel.isClosed()) {
			tunnel.close();
		}
	}

	@Override
	public Socket getSocket() {
		return socket;
	}
	
}
