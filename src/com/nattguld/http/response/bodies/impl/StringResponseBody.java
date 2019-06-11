package com.nattguld.http.response.bodies.impl;

import com.nattguld.http.response.bodies.IResponseBody;

/**
 * 
 * @author randqm
 *
 */

public class StringResponseBody implements IResponseBody<String> {
	
	/**
	 * The content.
	 */
	private final String content;
	
	
	/**
	 * Creates a new string response body.
	 * 
	 * @param content The content.
	 */
	public StringResponseBody(String content) {
		this.content = content;
	}

	@Override
	public String getBody() {
		return content;
	}

}
