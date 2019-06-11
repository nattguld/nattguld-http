package com.nattguld.http.content;

/**
 * 
 * @author randqm
 *
 */

public enum EncType {
	
	JSON("Json", "application/json", "application/json, text/javascript, */*; q=0.01"),
	XML("XML", "text/xml;charset=utf-8", "text/xml, application/xml, text/javascript, */*; q=0.01"),
	URL_ENCODED("URL encoded", "application/x-www-form-urlencoded; charset=UTF-8", "text/html,application/xhtml+xml,application/xml;q=0.9, image/webp,image/apng,*/*;q=0.8"),
	MULTIPART("Multipart", "multipart/form-data", "text/html,application/xhtml+xml,application/xml;q=0.9, image/webp,image/apng,*/*;q=0.8"),
	STREAM("Octet-Stream", "application/octet-stream", "*/*"),
	ALL("All", "*/*", "*/*"),
	PLAIN_TEXT("text/plain", "text/plain", "text/plain");
	
	
	/**
	 * The encoding name.
	 */
	private final String name;
	
	/**
	 * The content type header.
	 */
	private final String contentTypeHeader;
	
	/**
	 * The content accept header.
	 */
	private final String acceptHeader;
	
	
	/**
	 * Creates a new encoding type.
	 * 
	 * @param name The encoding name.
	 * 
	 * @param contentTypeHeader The content type header.
	 * 
	 * @param acceptHeader The content accept header.
	 */
	private EncType(String name, String contentTypeHeader, String acceptHeader) {
		this.name = name;
		this.contentTypeHeader = contentTypeHeader;
		this.acceptHeader = acceptHeader;
	}
	
	/**
	 * Retrieves the encoding name.
	 * 
	 * @return The encoding name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the content type header.
	 * 
	 * @return The content type header.
	 */
	public String getContentTypeHeader() {
		return contentTypeHeader;
	}
	
	/**
	 * Retrieves the content accept header.
	 * 
	 * @return The content accept header.
	 */
	public String getAcceptHeader() {
		return acceptHeader;
	}
	
	@Override
	public String toString() {
		return getName();
	}
 
}
