package com.nattguld.http.requests.impl;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;

/**
 * 
 * @author randqm
 *
 */

public class ConnectRequest extends Request {

	
	/**
	 * Creates a new connect request.
	 * 
	 * @param host The host address.
	 */
	public ConnectRequest(String host) {
		super(RequestType.CONNECT, host, 200, new Headers());
	}

	@Override
	public boolean hasBody() {
		return false;
	}

}
