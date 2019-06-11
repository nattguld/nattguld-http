package com.nattguld.http.requests.impl;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;

/**
 * 
 * @author randqm
 *
 */

public class HeadRequest extends Request {

	
	/**
	 * Creates a new head request.
	 * 
	 * @param url The target url.
	 */
	public HeadRequest(String url) {
		this(url, 200, null);
	}
	
	/**
	 * Creates a new head request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 */
	public HeadRequest(String url, int code) {
		this(url, code, null);
	}
	
	/**
	 * Creates a new head request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param headers The custom headers.
	 */
	public HeadRequest(String url, int code, Headers headers) {
		super(RequestType.HEAD, url, code, headers);
	}

	@Override
	public boolean hasBody() {
		return false;
	}

}
