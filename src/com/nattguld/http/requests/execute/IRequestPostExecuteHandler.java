package com.nattguld.http.requests.execute;

import com.nattguld.http.requests.Request;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public interface IRequestPostExecuteHandler {
	
	
	/**
	 * Handles a successful request.
	 * 
	 * @param request The request.
	 * 
	 * @param rr The request response.
	 */
	public void onSuccess(Request request, RequestResponse rr);
	
	/**
	 * Handles a failed request.
	 * 
	 * @param request The request.
	 * 
	 * @param rr The request response.
	 */
	public void onFail(Request request, RequestResponse rr);

}
