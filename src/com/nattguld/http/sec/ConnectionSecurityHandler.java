package com.nattguld.http.sec;

import java.util.ArrayList;
import java.util.List;

import com.nattguld.http.HttpClient;
import com.nattguld.http.requests.Request;
import com.nattguld.http.response.RequestResponse;

/**
 * 
 * @author randqm
 *
 */

public class ConnectionSecurityHandler {
	
	/**
	 * The security implementations.
	 */
	private final List<ConnectionSecurity> conSecImplementations = new ArrayList<>();
	
	
	/**
	 * Adds a connection security implementation.
	 */
	public void add(ConnectionSecurity conSec) {
		conSecImplementations.add(conSec);
	}
	
	/**
	 * Attempts to bypass a connection security implementation if present.
	 * 
	 * @param c The http client.
	 * 
	 * @param request The request.
	 * 
	 * @param The request response.
	 * 
	 * @return The result.
	 */
	public boolean bypass(HttpClient c, Request r, RequestResponse rr) {
		if (!hasImplementations()) {
			System.err.println("No connection security implementations found");
			return true;
		}
		for (ConnectionSecurity conSec : conSecImplementations) {
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

	/**
	 * Retrieves whether the security handler has implementations or not.
	 * 
	 * @return The result.
	 */
	public boolean hasImplementations() {
		return !conSecImplementations.isEmpty();
	}

}
