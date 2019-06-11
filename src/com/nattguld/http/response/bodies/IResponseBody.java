package com.nattguld.http.response.bodies;

import java.io.IOException;

/**
 * 
 * @author randqm
 *
 * @param <T>
 */

public interface IResponseBody<T extends Object> {

	
	/**
	 * Retrieves the response body.
	 * 
	 * @return The response body.
	 * 
	 * @throws IOException
	 */
	public T getBody();

}
