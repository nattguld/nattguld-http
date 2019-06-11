package com.nattguld.http.cfg;

/**
 * 
 * @author randqm
 *
 */

public enum HttpVersion {
	
	HTTP_1_0("HTTP/1.0"),
	HTTP_1_1("HTTP/1.1"),
	HTTP_2_0("HTTP/2.0");
	
	
	/**
	 * The protocol name.
	 */
	private final String name;
	
	
	/**
	 * Creates a new HTTP version.
	 * 
	 * @param name The protocol name.
	 */
	private HttpVersion(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the protocol name.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * Parses a http version string.
	 * 
	 * @param httpVersion The string representation.
	 * 
	 * @return The http version.
	 */
	public static HttpVersion parse(String httpVersion) {
		for (HttpVersion hv : values()) {
			if (hv.getName().equalsIgnoreCase(httpVersion)) {
				return hv;
			}
		}
		return HTTP_1_1;
	}

}
