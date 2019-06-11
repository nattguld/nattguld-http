package com.nattguld.http.requests.impl;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;

/**
 * 
 * @author randqm
 *
 */

public class GetRequest extends Request {

	/**
	 * Indicates whether the request referral should be used or not.
	 */
	private boolean noRef;
	
	
	/**
	 * Creates a new request.
	 * 
	 * @param url The target url.
	 */
	public GetRequest(String url) {
		this(url, 200);
	}
	
	/**
	 * Creates a new request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 */
	public GetRequest(String url, int code) {
		this(url, code, null);
	}
	
	/**
	 * Creates a new request.
	 * 
	 * @param url The target url.
	 * 
	 * @param headers The custom headers.
	 */
	public GetRequest(String url, Headers headers) {
		this(url, 200, headers);
	}
	
	/**
	 * Creates a new request.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param headers The custom headers.
	 */
	public GetRequest(String url, int code, Headers headers) {
		super(RequestType.GET, url, code, headers);
	}
	
	/**
	 * Modifies whether to log the referral or not.
	 * 
	 * @param noRef The new state.
	 * 
	 * @return The result.
	 */
	public GetRequest setNoRef(boolean noRef) {
		this.noRef = noRef;
		return this;
	}
	
	/**
	 * Retrieves whether to log the referral or not.
	 * 
	 * @return The result.
	 */
	public boolean isNoRef() {
		return noRef;
	}

	@Override
	public boolean hasBody() {
		return false;
	}
 
}
