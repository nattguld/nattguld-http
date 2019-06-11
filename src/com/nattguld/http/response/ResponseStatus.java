package com.nattguld.http.response;

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
	 * The response code.
	 */
	private final int code;
	
	/**
	 * The response message.
	 */
	private final String message;
	
	
	/**
	 * Creates a new response status.
	 * 
	 * @param code The response code.
	 * 
	 * @param message The response message.
	 */
	public ResponseStatus(int code, String message) {
		this(HttpVersion.HTTP_1_1, code, message);
	}
	
	/**
	 * Creates a new response status.
	 * 
	 * @param httpVersion The HTTP version.
	 * 
	 * @param code The response code.
	 * 
	 * @param message The response message.
	 */
	public ResponseStatus(HttpVersion httpVersion, int code, String message) {
		this.httpVersion = httpVersion;
		this.code = code;
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
	 * Retrieves the response code.
	 * 
	 * @return The response code.
	 */
	public int getCode() {
		return code;
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
		return getHttpVersion().getName() + " " + getCode() + " " + getMessage();
	}

}
