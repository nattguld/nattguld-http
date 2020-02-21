package com.nattguld.http.requests.headers.impl;

import java.util.List;
import java.util.Objects;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;
import com.nattguld.http.requests.headers.IheadersBuilder;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.util.NetUtil;

/**
 * 
 * @author randqm
 * @info https://w3c.github.io/webappsec-fetch-metadata/
 *
 */

public class SecFetchHeadersBuilder implements IheadersBuilder {

	/**
	 * The last contacted host.
	 */
	private final String lastContactedHost;
	
	/**
	 * The accessed hosts.
	 */
	private final List<String> accessedHosts;
	
	/**
	 * The previous hosts in the chain if applicable.
	 */
	private final List<Request> previous;
	
	
	/**
	 * The sec fetch headers builder.
	 * 
	 * @param lastContactedHost The last contacted host.
	 * 
	 * @param accessedHosts The accessed hosts.
	 * 
	 * @param previous The previous hosts in the chain if applicable.
	 */
	public SecFetchHeadersBuilder(String lastContactedHost, List<String> accessedHosts, List<Request> previous) {
		this.lastContactedHost = lastContactedHost;
		this.accessedHosts = accessedHosts;
		this.previous = previous;
	}
	
	@Override
	public void build(Request request, Headers headers) {
		headers.add("Sec-Fetch-Mode", request.getSecFetchMode()); 
		//TODO  nested-navigate  redirect to internal page by clicking button etc, no link? Form <<<
		//TODO same-origin/same-site/cross-site when called inside the page, like js scripts being called
		
		String host = NetUtil.getDomain(request.getUrl());
		boolean firstHostContact = !accessedHosts.contains(host) || (request.getRequestType() == RequestType.GET && ((GetRequest)request).isNoRef());
		boolean sameOrigin = !firstHostContact && isSameOrigin(host, lastContactedHost);
		boolean sameSite = !sameOrigin && isSameSiteDifferentHost(host, lastContactedHost);
		boolean crossSite = !sameOrigin && !sameSite;
		
		if (!firstHostContact && !crossSite && previous.size() > 1 && Objects.nonNull(lastContactedHost)) {
			Request initialReq = previous.get(0);
			String initialHost = NetUtil.getDomain(initialReq.getUrl());
			
			for (int i = 1; i < previous.size(); i++) {
				Request prevReq = previous.get(i);
				String prevHost = NetUtil.getDomain(prevReq.getUrl());
				
				if (isSameOrigin(initialHost, prevHost)) {
					continue;
				}
				if (isSameSiteDifferentHost(initialHost, prevHost)) {
					sameSite = true;
					continue;
				}
				crossSite = true;
				break;
			}
		}
		if (firstHostContact) {
			headers.add("Sec-Fetch-Site", "none");
		} else if (crossSite) {
			headers.add("Sec-Fetch-Site", "cross-site");
		} else if (sameSite) {
			headers.add("Sec-Fetch-Site", "same-site");
		} else {
			headers.add("Sec-Fetch-Site", "same-origin");
		}
		if (!request.isXMLHttpRequest()) {
			headers.add("Sec-Fetch-User", "?1");
		}
	}
	
	/**
	 * Retrieves whether a given host has the same origin as another host.
	 * 
	 * @param host The host.
	 * 
	 * @param otherHost The other host.
	 * 
	 * @return The result.
	 */
	private static boolean isSameOrigin(String host, String otherHost) {
		return Objects.isNull(otherHost) || host.equals(otherHost);
	}
	
	/**
	 * Retrieves whether the host is different from an other host but on the same domain.
	 * 
	 * @param host The host.
	 * 
	 * @param otherHost The other host.
	 * 
	 * @return The result.
	 */
	private static boolean isSameSiteDifferentHost(String host, String otherHost) {
		if (Objects.isNull(otherHost) || host.equals(otherHost)) {
			return false;
		}
		return host.contains(otherHost) || otherHost.contains(host);
	}
 
}
