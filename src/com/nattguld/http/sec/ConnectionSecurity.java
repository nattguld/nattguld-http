package com.nattguld.http.sec;

import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.Request;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public interface ConnectionSecurity {
	

	/**
	 * Retrieves whether security has been encountered or not.
	 * 
	 * @param c The client session.
	 * 
	 * @param request The request.
	 * 
	 * @param r The request response.
	 * 
	 * @return The result.
	 * 
	 * @exception Exception
	 */
	public boolean encountered(HttpClient c, Request request, RequestResponse r) throws Exception;
	
	/**
	 * Attempts to bypass the security.
	 * 
	 * @param c The client session.
	 * 
	 * @param request The request.
	 * 
	 * @param r The request response.
	 * 
	 * @return Whether the security has been bypassed or not.
	 * 
	 * @exception Exception
	 */
	public boolean bypass(HttpClient c, Request request, RequestResponse r) throws Exception;
	
	/**
	 * Retrieves the name of the security implementation.
	 * 
	 * @return The name.
	 */
	public String getName();

}
