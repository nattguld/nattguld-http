package com.nattguld.http.content.cookies;

/**
 * 
 * @author randqm
 *
 */

public class Cookie {
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The value.
	 */
	private final String value;
	
	/**
	 * The expiration.
	 */
	private final String expires;
	
	/**
	 * The path.
	 */
	private final String path;
	
	/**
	 * The domain.
	 */
	private final String domain;
	
	/**
	 * The security.
	 */
	private final boolean secure;
	
	/**
	 * The max age of the cookie.
	 */
	private final long maxAge;
	
	
	/**
	 * Creates a new cookie.
	 * 
	 * @param name The name.
	 * 
	 * @param value The value.
	 * 
	 * @param domain The domain.
	 */
	public Cookie(String name, String value, String domain) {
		this(name, value, "/", domain);
	}
	
	/**
	 * Creates a new cookie.
	 * 
	 * @param name The name.
	 * 
	 * @param value The value.
	 * 
	 * @param expires The expiration.
	 * 
	 * @param domain The domain.
	 */
	public Cookie(String name, String value, String expires, String domain) {
		this(name, value, expires, "/", domain);
	}
	
	/**
	 * Creates a new cookie.
	 * 
	 * @param name The name.
	 * 
	 * @param value The value.
	 * 
	 * @param expires The expiration.
	 * 
	 * @param path The path.
	 * 
	 * @param domain The domain.
	 */
	public Cookie(String name, String value, String expires, String path, String domain) {
		this(name, value, expires, path, domain, false);
	}
	
	/**
	 * Creates a new cookie.
	 * 
	 * @param name The name.
	 * 
	 * @param value The value.
	 * 
	 * @param expires The expiration.
	 * 
	 * @param path The path.
	 * 
	 * @param domain The domain.
	 * 
	 * @param secure The security.
	 */
	public Cookie(String name, String value, String expires, String path, String domain, boolean secure) {
		this(name, value, expires, path, domain, secure, 0L);
	}
	
	/**
	 * Creates a new cookie.
	 * 
	 * @param name The name.
	 * 
	 * @param value The value.
	 * 
	 * @param expires The expiration.
	 * 
	 * @param path The path.
	 * 
	 * @param domain The domain.
	 * 
	 * @param secure The security.
	 * 
	 * @param maxAge The max age of the cookie.
	 */
	public Cookie(String name, String value, String expires, String path, String domain, boolean secure, long maxAge) {
		this.name = name;
		this.value = value;
		this.expires = expires;
		this.path = path;
		this.domain = domain;
		this.secure = secure;
		this.maxAge = maxAge;
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
	 * Retrieves the value.
	 * 
	 * @return The value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Retrieves the expiration.
	 * 
	 * @return The expiration.
	 */
	public String getExpires() {
		return expires;
	}

	/**
	 * Retrieves the path.
	 * 
	 * @return The path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Retrieves the domain.
	 * 
	 * @return The domain.
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Retrieves the security.
	 * 
	 * @return The security.
	 */
	public boolean isSecure() {
		return secure;
	}
	
	/**
	 * Retrieves the max age of the cookie.
	 * 
	 * @return The max age.
	 */
	public long getMaxAge() {
		return maxAge;
	}
	
	@Override
	public String toString() {
		return getName() + "=" + getValue();
	}

}
