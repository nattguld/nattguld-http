package com.nattguld.http.requests;

import java.util.Objects;

import com.nattguld.http.content.ContentBody;
import com.nattguld.http.headers.Headers;

/**
 * 
 * @author randqm
 *
 */

public abstract class ContentRequest extends Request {

	/**
	 * The content body.
	 */
	private final ContentBody<?> body;
	
	
	/**
	 * Creates a new content request.
	 * 
	 * @param requestType The request type.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param body The content body.
	 * 
	 * @param headers The custom headers.
	 */
	public ContentRequest(RequestType requestType, String url, int code, ContentBody<?> body, Headers headers) {
		super(requestType, url, code, headers);
		
		this.body = body;
	}
	
	@Override
	public boolean hasBody() {
		return Objects.nonNull(getBody());
	}
	
	/**
	 * Retrieves the content body.
	 * 
	 * @return The content body.
	 */
	public ContentBody<?> getBody() {
		return body;
	}
	
}
