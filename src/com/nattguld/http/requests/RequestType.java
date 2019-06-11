package com.nattguld.http.requests;

/**
 * 
 * @author randqm
 *
 */

public enum RequestType {
    
    POST("POST", true),
    PUT("PUT", false),
    GET("GET", true),
    OPTIONS("OPTIONS", true),
    DELETE("DELETE", true), 
    HEAD("HEAD", false),
    TRACE("TRACE", false),
    CONNECT("CONNECT", false),
    PATCH("PATCH", true);
    
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * Whether the request can receive a response with body or not.
	 */
	private final boolean body;
	
	
	/**
	 * Creates a new request type.
	 * 
	 * @param name The name.
	 * 
	 * @param body Whether the request can receive a response with body or not.
	 */
	private RequestType(String name, boolean body) {
		this.name = name;
		this.body = body;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves whether the request can receive a response with body or not.
	 * 
	 * @return The result.
	 */
	public boolean isBody() {
		return body;
	}
    
    @Override
    public String toString() {
    	return getName();
    }

}
