package com.nattguld.http.requests.impl;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;

/**
 * 
 * @author randqm
 *
 */

public class OptionsRequest extends Request {
	
	/**
	 * The request type option.
	 */
	private final RequestType requestTypeOption;
	
	
	/**
	 * Creates a new request.
	 * 
	 * @param requestType The request type.
	 * 
	 * @param url The target url.
	 */
	public OptionsRequest(RequestType requestTypeOption, String url) {
		this(requestTypeOption, url, 200);
	}
	
	/**
	 * Creates a new request.
	 * 
	 * @param requestType The request type.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 */
	public OptionsRequest(RequestType requestTypeOption, String url, int code) {
		this(requestTypeOption, url, code, null);
	}
	
	/**
	 * Creates a new request.
	 * 
	 * @param requestType The request type.
	 * 
	 * @param url The target url.
	 * 
	 * @param headers The custom headers.
	 */
	public OptionsRequest(RequestType requestTypeOption, String url, Headers headers) {
		this(requestTypeOption, url, 200, headers);
	}
	
	/**
	 * Creates a new request.
	 * 
	 * @param requestTypeOption The request type.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param headers The custom headers.
	 */
	public OptionsRequest(RequestType requestTypeOption, String url, int code, Headers headers) {
		super(RequestType.OPTIONS, url, code, headers);
		
		this.requestTypeOption = requestTypeOption;
	}

	@Override
	public boolean hasBody() {
		return false;
	}
	
	/**
	 * Retrieves the request type option.
	 * 
	 * @return The request type option.
	 */
	public RequestType getRequestTypeOption() {
		return requestTypeOption;
	}
 
}
