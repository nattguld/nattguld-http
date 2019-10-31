package com.nattguld.http.pooling;

import java.util.Objects;

import com.nattguld.http.socket.HttpSocket;
import com.nattguld.util.pooling.ObjectPool;

/**
 * 
 * @author randqm
 *
 */

public class SocketPool extends ObjectPool<HttpSocket> {

	/**
	 * The singleton instance.
	 */
	private static SocketPool singleton;
	
	
	/**
	 * Creates a new socket pool.
	 */
	public SocketPool() {
		super(0);
	}
	
	@Override
	protected HttpSocket createElement() {
		return new HttpSocket();
	}
	
	/**
	 * Retrieves the singleton instance.
	 * 
	 * @return The singleton instance.
	 */
	public static SocketPool getSingleton() {
		if (Objects.isNull(singleton)) {
			singleton = new SocketPool();
		}
		return singleton;
	}

}
