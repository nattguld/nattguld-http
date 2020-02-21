package com.nattguld.http.requests.execute;

import java.util.ArrayDeque;
import java.util.Deque;

import com.nattguld.http.requests.Request;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public class RequestBuilder {
	
	/**
	 * The main request.
	 */
	private final Request mainRequest;
	
	/**
	 * Holds the background requests.
	 */
	private final Deque<Request> backgroundRequests;
	
	
	/**
	 * Creates a new request builder.
	 * 
	 * @param mainRequest The main request.
	 */
	public RequestBuilder(Request mainRequest) {
		this.mainRequest = mainRequest;
		this.backgroundRequests = new ArrayDeque<>();
	}
	
	/**
	 * Adds a new background request.
	 * 
	 * @param request The request.
	 * 
	 * @return The request builder.
	 */
	public RequestBuilder addBackgroundRequest(Request request) {
		backgroundRequests.offer(request);
		return this;
	}
	
	/**
	 * Executes the requests.
	 * 
	 * @param requestExecutor The request executor to handle execution.
	 * 
	 * @return The request response for the main request.
	 */
	public RequestResponse execute(RequestExecutor requestExecutor) {
		RequestResponse mainResponse = execute(requestExecutor, mainRequest);
		
		if (!mainResponse.validate() || backgroundRequests.isEmpty()) {
			return mainResponse;
		}
		while (!backgroundRequests.isEmpty()) { ////TODO same-origin/same-site/cross-site when called inside the page, like js scripts being called  cors vs no-cors etc
			execute(requestExecutor, backgroundRequests.poll());
		}
		return mainResponse;
	}
	
	/**
	 * Executes a given request.
	 * 
	 * @param requestExecutor The request executor.
	 * 
	 * @param request The request.
	 * 
	 * @return The request response.
	 */
	private RequestResponse execute(RequestExecutor requestExecutor, Request request) {
		RequestResponse rr = requestExecutor.execute(request);
		
		if (!rr.validate()) {
			request.getPostExecuteHandler().onFail(request, rr);
			return rr;
		}
		request.getPostExecuteHandler().onSuccess(request, rr);
		return rr;
	}

}
