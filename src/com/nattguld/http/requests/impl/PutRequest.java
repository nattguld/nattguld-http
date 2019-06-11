package com.nattguld.http.requests.impl;

import com.nattguld.http.content.ContentBody;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.ContentRequest;
import com.nattguld.http.requests.RequestType;

/**
 * 
 * @author randqm
 *
 */

public class PutRequest extends ContentRequest {

	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param body The content body.
	 * 
	 * @param headers The custom headers.
	 */
	public PutRequest(String url, int code, ContentBody<?> body, Headers headers) {
		super(RequestType.PUT, url, code, body, headers);
	}

}
