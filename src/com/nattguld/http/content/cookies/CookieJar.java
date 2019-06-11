package com.nattguld.http.content.cookies;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author randqm
 *
 */

public class CookieJar {
	
	/**
	 * The cookies.
	 */
	private final List<Cookie> cookies = new ArrayList<>();
	

	/**
	 * Adds a cookie.
	 * 
	 * @param cookie The cookie to add.
	 */
	public void add(Cookie cookie) {
		cookies.add(cookie);
	}
	
	/**
	 * Replaces or adds a cookie of not present.
	 * 
	 * @param cookie The cookie.
	 */
	public void replaceOrAdd(Cookie cookie) {
		remove(getByName(cookie.getName()));
		add(cookie);
	}
	
	/**
	 * Imports cookies into the cookie jar.
	 * 
	 * @param cookies The cookies to import.
	 */
	public void importCookies(List<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			replaceOrAdd(cookie);
		}
	}
	
	/**
	 * Removes a cookie.
	 * 
	 * @param cookie The cookie.
	 */
	public void remove(Cookie cookie) {
		cookies.remove(cookie);
	}
	
	/**
	 * Retrieves a cookie by it's name.
	 * 
	 * @param name The name.
	 * 
	 * @return The cookie.
	 */
	public Cookie getByName(String name) {
		return cookies.stream()
				.filter(c -> c.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Empties the cookie jar.
	 */
	public void empty() {
		cookies.clear();
	}
	
	/**
	 * Retrieves whether the cookie jar is empty or not.
	 * 
	 * @return The result.
	 */
	public boolean isEmpty() {
		return cookies.isEmpty();
	}
	
	/**
	 * Retrieves the cookies.
	 * 
	 * @return The cookies.
	 */
	public List<Cookie> getCookies() {
		return cookies;
	}

}
