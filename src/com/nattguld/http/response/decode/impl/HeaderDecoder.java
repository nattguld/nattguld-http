package com.nattguld.http.response.decode.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.nattguld.http.HTTPCode;
import com.nattguld.http.cfg.HttpVersion;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.cookies.Cookie;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.response.ResponseStatus;
import com.nattguld.http.response.decode.IResponseDecoder;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class HeaderDecoder implements IResponseDecoder {

	/**
	 * The status response.
	 */
	private ResponseStatus rs = new ResponseStatus(HttpVersion.HTTP_1_1, HTTPCode.INVALID, "Header not found in decoding process");
	
	/**
	 * The response headers.
	 */
	private Headers headers = new Headers();
	
	/**
	 * The response cookies.
	 */
	private List<Cookie> cookies = new ArrayList<>();
	
	
	@Override
	public InputStream decode(InputStream in) throws IOException {
		List<Byte> lineBuffer = new LinkedList<>();
		
		while (true) {
			byte b = (byte)in.read();
			
			if (b < 0) {
				System.err.println("Malformed server response received, EOF reached unexpectedly");
				return null;
			}
			lineBuffer.add(b);
			
			if (b != '\n') { //Keep collecting bytes if there's no new line indicated
				continue;
			}
			byte[] lineBufferArray = new byte[lineBuffer.size()];
			
			for (int i = 0; i < lineBufferArray.length; i++) {
				lineBufferArray[i] = lineBuffer.get(i);
			}
			String s = new String(lineBufferArray, "ASCII7").trim(); //Build a string out of our collected bytes and trim off the line break
			lineBuffer.clear(); //Clears the line buffer for the next read
			
			if (s.isEmpty()) { //When an empty line is found it means we parsed all headers
				break;
			}
			if (s.startsWith("HTTP")) { //The status line of the response
				String[] msgParts = s.split(" ");
				String httpProtocolVersion = msgParts[0];
				String responseCode = msgParts[1];
				StringBuilder responseMsgBuilder = new StringBuilder();
				
				for (int i = 2; i < msgParts.length; i ++) {
					responseMsgBuilder.append(msgParts[i] + " ");
				}
				int code = Integer.parseInt(responseCode);
				HTTPCode httpCode = HTTPCode.getForCode(code);
				String msg = responseMsgBuilder.toString().trim();
				
				if (msg.isEmpty()) {
					msg = httpCode.getMessage();
				}
				if (httpCode == HTTPCode.INVALID) {
					msg = "[Unhandled code: " + code + "] - " + msg;
				}
				rs = new ResponseStatus(HttpVersion.parse(httpProtocolVersion), httpCode, msg);
				continue;
			}
			if (s.equals("\r\n")) {
				break;
			}
			String key = s.substring(0, s.indexOf(":")).trim();
			String value = s.substring(s.indexOf(":") + 1, s.length()).trim();
			
			if (!key.equalsIgnoreCase("Set-Cookie")) {
				headers.add(key, value);
				
				if (NetConfig.getConfig().isDebug()) {
					System.out.println("RESPONSE-HEADER => " + key + ": " + value);
				}
				continue;
			}
			Cookie cookie = extractCookie(value);
			cookies.add(cookie);
			
			if (NetConfig.getConfig().isDebug()) {
				System.out.println("RESPONSE-COOKIE => " + cookie.getName() + ": " + cookie.getValue());
			}
		}
		return in;
	}
	
	/**
	 * Extracts a cookie from a header value.
	 * 
	 * @param headerValue The header value.
	 * 
	 * @return The cookie.
	 */
	private Cookie extractCookie(String headerValue) {
		String[] fields = headerValue.split(";");
		
		String key = fields[0].substring(0, fields[0].indexOf("=")).trim();
		String value = fields[0].substring(fields[0].indexOf("=") + 1, fields[0].length());
		String expires = null;
		String path = "/";
		String domain = "/";
		boolean secure = false;
		@SuppressWarnings("unused")
		boolean httpOnly = false;
		long maxAge = 0L;
		
		for (int i = 1; i < fields.length; i ++) {
			String field = fields[i].trim();
			
			if (field.equalsIgnoreCase("secure")) {
				secure = true;
				continue;
			}
			if (field.equalsIgnoreCase("HttpOnly")) {
				httpOnly = true;
				continue;
			}
			if (!field.contains("=")) {
				System.err.println("Malformed cookie field: " + fields[i]);
				continue;
			}
			String fieldKey = field.substring(0, field.indexOf("=")).trim();
			String fieldValue = field.substring(field.indexOf("=") + 1, field.length());

			if (fieldKey.equalsIgnoreCase("expires")) {
				expires = fieldValue;
				continue;
			}
			if (fieldKey.equalsIgnoreCase("domain")) {
				domain = fieldValue;
				continue;
			}
			if (fieldKey.equalsIgnoreCase("path")) {
				path = fieldValue;
				continue;
			}
			if (fieldKey.equalsIgnoreCase("Max-Age")) {
				maxAge = Maths.parseLong(fieldValue, 0L);
				continue;
			}
			System.err.println("Unhandled cookie field: " + fields[i]);
		}
		return new Cookie(key, value, expires, path, domain, secure, maxAge);
	}
	
	/**
	 * Retrieves the response status.
	 * 
	 * @return The response status.
	 */
	public ResponseStatus getResponseStatus() {
		return rs;
	}
	
	/**
	 * Retrieves the response headers.
	 * 
	 * @return The response headers.
	 */
	public Headers getHeaders() {
		return headers;
	}
	
	/**
	 * Retrieves the response cookies.
	 * 
	 * @return The response cookies.
	 */
	public List<Cookie> getCookies() {
		return cookies;
	}

}
