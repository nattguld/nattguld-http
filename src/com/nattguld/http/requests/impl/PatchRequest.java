package com.nattguld.http.requests.impl;

import com.nattguld.http.content.ContentBody;
import com.nattguld.http.content.bodies.EmptyBody;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.ContentRequest;
import com.nattguld.http.requests.RequestType;

/**
 * 
 * @author randqm
 *
 */

public class PatchRequest extends ContentRequest {

	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 */
	public PatchRequest(String url) {
		super(RequestType.PATCH, url, 200, new EmptyBody(), null);
	}
	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 */
	public PatchRequest(String url, int code) {
		super(RequestType.PATCH, url, code, new EmptyBody(), null);
	}
	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param body The content body.
	 */
	public PatchRequest(String url, ContentBody<?> body) {
		super(RequestType.PATCH, url, 200, body, null);
	}
	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param body The content body.
	 */
	public PatchRequest(String url, int code, ContentBody<?> body) {
		super(RequestType.PATCH, url, code, body, null);
	}
	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param headers The custom headers.
	 */
	public PatchRequest(String url, int code, Headers headers) {
		super(RequestType.PATCH, url, code, new EmptyBody(), headers);
	}
	
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
	public PatchRequest(String url, Headers headers) {
		super(RequestType.PATCH, url, 200, new EmptyBody(), headers);
	}
	
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
	public PatchRequest(String url, int code, ContentBody<?> body, Headers headers) {
		super(RequestType.PATCH, url, code, body, headers);
	}

}
