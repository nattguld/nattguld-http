package com.nattguld.http.proxies;

import java.util.Objects;

import com.nattguld.util.generics.kvps.impl.StringKeyValuePair;
import com.nattguld.util.hashing.Hasher;

/**
 * 
 * @author randqm
 *
 */

public class ProxyAuthCredentials extends StringKeyValuePair {

	/**
	 * The base 64 auth.
	 */
	private String base64Auth;
	
	
	/**
	 * Creates new credentials.
	 * 
	 * @param username The username.
	 * 
	 * @param password The password.
	 */
	public ProxyAuthCredentials(String username, String password) {
		super(username, password);
	}
	
	/**
	 * Retrieves the base64 authentication.
	 * 
	 * @return The base64 authentication.
	 */
	public String getBase64Auth() {
		if (Objects.isNull(base64Auth)) {
			this.base64Auth = Hasher.base64(getKey() + ":" + getValue());
		}
		return base64Auth;
	}
	
	@Override
	public String toString() {
		return getKey() + ":" + getValue();
	}

}
