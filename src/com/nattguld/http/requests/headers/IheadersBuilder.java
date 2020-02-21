package com.nattguld.http.requests.headers;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;

/**
 * 
 * @author randqm
 *
 */

public interface IheadersBuilder {
	
	
	/**
	 * Builds the headers.
	 * 
	 * @param request The request.
	 * 
	 * @param headers The headers container.
	 */
	public void build(Request request, Headers headers);

}