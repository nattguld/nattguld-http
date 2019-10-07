package com.nattguld.http.response;

import com.nattguld.http.HTTPCode;
import com.nattguld.http.cfg.HttpVersion;

/**
 * 
 * @author randqm
 *
 */

public class ResponseStatus {
	
	/**
	 * The HTTP version.
	 */
	private final HttpVersion httpVersion;
	
	/**
	 * The HTTP code.
	 */
	private final HTTPCode httpCode;
	
	/**
	 * The response message.
	 */
	private final String message;
	
	
	/**
	 * Creates a new response status.
	 * 
	 * @param httpCode The HTTP code.
	 * 
	 * @param message The response message.
	 */
	public ResponseStatus(HTTPCode httpCode, String message) {
		this(HttpVersion.HTTP_1_1, httpCode, message);
	}
	
	/**
	 * Creates a new response status.
	 * 
	 * @param httpVersion The HTTP version.
	 * 
	 * @param httpCode The HTTP code code.
	 * 
	 * @param message The response message.
	 */
	public ResponseStatus(HttpVersion httpVersion, HTTPCode httpCode, String message) {
		this.httpVersion = httpVersion;
		this.httpCode = httpCode;
		this.message = message;
	}
	
	/**
	 * Retrieves the HTTP version.
	 * 
	 * @return The HTTP version.
	 */
	public HttpVersion getHttpVersion() {
		return httpVersion;
	}
	
	/**
	 * Retrieves the HTTP code.
	 * 
	 * @return The HTTP code.
	 */
	public HTTPCode getHttpCode() {
		return httpCode;
	}
	
	/**
	 * Retrieves the response message.
	 * 
	 * @return The response message.
	 */
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return getHttpVersion().getName() + " " + getHttpCode().toString() + " " + getMessage();
	}

}
