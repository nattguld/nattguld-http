package com.nattguld.http.headers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author randqm
 *
 */

public class Headers {
	
	/**
	 * THolds the header key-value pairs.
	 */
	private final Map<String, String> headers = new LinkedHashMap<>();
	
	
	/**
	 * Adds headers from other headers.
	 * 
	 * @param headers The headers.
	 */
	public void add(Headers headers) {
		for (Entry<String, String> entry : headers.getHeaders().entrySet()) {
			add(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Adds a new header entry.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 */
	public void add(String key, String value) {
		headers.put(key, value);
	}
	
	/**
	 * Removes a header entry.
	 * 
	 * @param key The key.
	 */
	public void remove(String key) {
		if (!headers.containsKey(key)) {
			return;
		}
		headers.remove(key);
	}
	
	/**
	 * Retrieves a header value for a given key.
	 * 
	 * @param name The key.
	 * 
	 * @return The value.
	 */
	public String getValue(String key) {
		return headers.get(key);
	}
	
	/**
	 * Retrieves a header value for a given key, ignoring capitalization.
	 * 
	 * @param key The key.
	 * 
	 * @return The value.
	 */
	public String getValueIgnoreCase(String key) {
		for (String o : headers.keySet()) {
			if (o.equalsIgnoreCase(key)) {
				return getValue(o);
			}
		}
		return null;
	}
	
	/**
	 * Retrieves the headers.
	 * 
	 * @return The headers.
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

}
