package com.nattguld.http.sec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.Request;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.http.util.NetUtil;

/**
 * 
 * @author randqm
 *
 */

public class ConnectionSecurityHandler {
	
	/**
	 * The security implementations.
	 */
	private static Map<ConnectionSecurity, String> conSecImplementations = new HashMap<>();
	
	
	/**
	 * Registers a connection security implementation for a given domain.
	 * 
	 * @param conSec The connection security implementation.
	 * 
	 * @param domain The domain.
	 */
	public static void register(ConnectionSecurity conSec, String domain) {
		conSecImplementations.put(conSec, domain);
	}
	
	/**
	 * Retrieves the implementations for a given domain.
	 * 
	 * @param domain The domain.
	 * 
	 * @return The implementations.
	 */
	private static List<ConnectionSecurity> getImplementations(String domain) {
		List<ConnectionSecurity> implementations = new ArrayList<>();
		
		for (Entry<ConnectionSecurity, String> entry : conSecImplementations.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(domain)) {
				implementations.add(entry.getKey());
			}
		}
		return implementations;
	}
	
	/**
	 * Attempts to bypass security.
	 * 
	 * @param c The client session.
	 * 
	 * @param r The request.
	 * 
	 * @param rr The request response after initial request without attempting to bypass security.
	 * 
	 * @return Whether the bypass was successful or not.
	 */
	public static boolean bypass(HttpClient c, Request r, RequestResponse rr) {
		String domain = NetUtil.getDomain(r.getUrl());
		
		List<ConnectionSecurity> implementations = getImplementations(domain);
		
		if (implementations.isEmpty()) {
			return true;
		}
		for (ConnectionSecurity conSec : implementations) {
			try {
				if (!conSec.encountered(c, r, rr)) {
					continue;
				}
				System.out.println("[" + conSec.getName() + "][" + r.getUrl() + "]: Connection security encountered");
				
				if (!conSec.bypass(c, r, rr)) {
					System.err.println("[" + conSec.getName() + "][" + r.getUrl() + "]: Failed to by pass connection security");
					return false;
				}
				if (conSec.encountered(c, r, rr)) {
					System.err.println("[" + conSec.getName() + "][" + r.getUrl() + "]: Connection security still encountered after bypassing");
					return false;
				}
				System.out.println("[" + conSec.getName() + "][" + r.getUrl() + "]: Bypassed connection security");
				
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
