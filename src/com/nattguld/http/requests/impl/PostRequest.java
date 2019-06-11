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

public class PostRequest extends ContentRequest {

	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 */
	public PostRequest(String url) {
		super(RequestType.POST, url, 200, new EmptyBody(), null);
	}
	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 */
	public PostRequest(String url, int code) {
		super(RequestType.POST, url, code, new EmptyBody(), null);
	}
	
	/**
	 * Creates a new post request.
	 * 
	 * @param url The target url.
	 * 
	 * @param body The content body.
	 */
	public PostRequest(String url, ContentBody<?> body) {
		super(RequestType.POST, url, 200, body, null);
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
	public PostRequest(String url, int code, ContentBody<?> body) {
		super(RequestType.POST, url, code, body, null);
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
	public PostRequest(String url, int code, Headers headers) {
		super(RequestType.POST, url, code, new EmptyBody(), headers);
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
	public PostRequest(String url, Headers headers) {
		super(RequestType.POST, url, 200, new EmptyBody(), headers);
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
	public PostRequest(String url, int code, ContentBody<?> body, Headers headers) {
		super(RequestType.POST, url, code, body, headers);
	}

}
