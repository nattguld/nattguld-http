package com.nattguld.http.proxies;

/**
 * 
 * @author randqm
 *
 */

public enum ProxyType {
	
	HTTP,
	HTTPS,
	SOCKS4,
	SOCKS5;
	
	
	@Override
	public String toString() {
		return name();
	}

}
