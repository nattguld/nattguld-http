package com.nattguld.http.requests.headers.impl;

import java.util.Objects;

import com.nattguld.http.content.cookies.CookieJar;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.ContentRequest;
import com.nattguld.http.requests.Request;
import com.nattguld.http.requests.RequestType;
import com.nattguld.http.requests.headers.IheadersBuilder;
import com.nattguld.http.requests.impl.OptionsRequest;
import com.nattguld.http.util.NetUtil;

/**
 * 
 * @author randqm
 *
 */

public class DefaultHeadersBuilder implements IheadersBuilder {

	/**
	 * The user agent.
	 */
	private final String userAgent;
	
	/**
	 * The language.
	 */
	private final String language;
	
	/**
	 * The last referer.
	 */
	private final String lastReferer;
	
	/**
	 * Whether do not track is enabled or not.
	 */
	private final boolean isDoNotTrack;
	
	/**
	 * Whether it's a redirect request or not.
	 */
	private final boolean redirect;
	
	/**
	 * The cookie jar.
	 */
	private final CookieJar cookieJar;
	
	
	/**
	 * Creates a default headers builder.
	 * 
	 * @param userAgent The user agent.
	 * 
	 * @param language The language.
	 * 
	 * @param lastReferer The last referer.
	 * 
	 * @param isDoNotTrack Whether do not track is enabled or not.
	 * 
	 * @param redirect Whether it's a redirect request or not.
	 * 
	 * @param cookieJar The cookie jar.
	 */
	public DefaultHeadersBuilder(String userAgent, String language, String lastReferer
			, boolean isDoNotTrack, boolean redirect, CookieJar cookieJar) {
		this.userAgent = userAgent;
		this.language = language;
		this.lastReferer = lastReferer;
		this.isDoNotTrack = isDoNotTrack;
		this.redirect = redirect;
		this.cookieJar = cookieJar;
	}
	
	@Override
	public void build(Request request, Headers headers) {
		String host = NetUtil.getDomain(request.getUrl());
		
		headers.add("Host", host);
		headers.add("Connection", "keep-alive");
		
		if ((request.getRequestType() == RequestType.POST && !request.isXMLHttpRequest()) || redirect || Objects.nonNull(request.getCacheControl())) {
			headers.add("Cache-Control", Objects.nonNull(request.getCacheControl()) ? request.getCacheControl() : "max-age=0");
		}
		if (!request.isXMLHttpRequest()) {
			headers.add("Upgrade-Insecure-Requests", "1");
		}
		headers.add("Accept", request.getResponseEncType().getAcceptHeader());
		
		if (request.isXMLHttpRequest()) {
			headers.add("X-Requested-With", "XMLHttpRequest");
		}
		if (request.isXMLHttpRequest() || request instanceof ContentRequest) {
			headers.add("Origin", NetUtil.getBaseUrl(request.getUrl()));
		}
		headers.add("User-Agent", userAgent);
		
		if (isDoNotTrack) {
			headers.add("DNT", "1");
		}
        if (Objects.nonNull(lastReferer)) {
        	headers.add("Referer", lastReferer);
        }
        headers.add("Accept-Encoding", "gzip, deflate, br");
		headers.add("Accept-Language", language);
		
		if (request.getRequestType() == RequestType.OPTIONS) { //Sets the request method for options request
			headers.add("Access-Control-Request-Method", ((OptionsRequest)request).getRequestTypeOption().getName()); //Sets the option request header
			headers.add("Origin", NetUtil.getBaseUrl(request.getUrl()));
		}
		if (request.getRequestType() != RequestType.OPTIONS && Objects.nonNull(cookieJar) && !cookieJar.isEmpty()) {
			StringBuilder cookieSb = new StringBuilder();
			
			for (int i = 0; i < cookieJar.getCookies().size(); i++) {
				cookieSb.append(cookieJar.getCookies().get(i).getName() + "=" + cookieJar.getCookies().get(i).getValue());
				
				if (i < (cookieJar.getCookies().size() - 1)) {
					cookieSb.append("; ");
				}
			}
			headers.add("Cookie", cookieSb.toString());
		}
		if (request.hasBody()) {
			((ContentRequest)request).getBody().prepare(headers);
		}
	}

}
