package com.nattguld.http.requests;

import java.util.Objects;

import com.nattguld.http.DataCounter;
import com.nattguld.http.content.EncType;
import com.nattguld.http.headers.Headers;

/**
 * 
 * @author randqm
 *
 */

public abstract class Request {
	
	/**
	 * The target url.
	 */
	private final String url;
	
	/**
	 * The expected response code.
	 */
	private final int code;
	
	/**
	 * The request type.
	 */
	private final RequestType requestType;
	
	/**
	 * The custom headers.
	 */
	private final Headers headers;
	
	/**
	 * The amount of times the request has been attempted.
	 */
	private int attempts;
	
	/**
	 * Retrieves the expected response encoding type.
	 */
	private EncType responseEncType;
	
	/**
	 * Whether the request is an XML HTTP request.
	 */
	private boolean xmlHttpRequest;
	
	/**
	 * The data counter.
	 */
	private final DataCounter dataCounter;
	
	/**
	 * The cache control.
	 */
	private String cacheControl;
	
	/**
	 * The save path for the request response.
	 */
	private String savePath;
	
	/**
	 * The progress listener.
	 */
	private RequestProgressListener progressListener;
	
	/**
	 * Whether to decode the body or not.
	 */
	private boolean decodeBody;
	
	/**
	 * The port.
	 */
	private int port = 80;
	
	
	/**
	 * Creates a new request.
	 * 
	 * @param requestType The request type.
	 * 
	 * @param url The target url.
	 * 
	 * @param code The expected response code.
	 * 
	 * @param headers The custom headers.
	 */
	public Request(RequestType requestType, String url, int code, Headers headers) {
		this.requestType = requestType;
		this.url = url;
		this.code = code;
		this.headers = Objects.isNull(headers) ? new Headers() : headers;
		this.responseEncType = EncType.URL_ENCODED;
		this.decodeBody = true;
		this.dataCounter = new DataCounter();
	}
	
	/**
	 * Whether the request has a body or not.
	 * 
	 * @return The result.
	 */
	public abstract boolean hasBody();
	
	/**
	 * Retrieves the request type.
	 * 
	 * @return The request type.
	 */
	public RequestType getRequestType() {
		return requestType;
	}
	
	/**
	 * Retrieves the target url.
	 * 
	 * @return The target url.
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Retrieves the expected response code.
	 * 
	 * @return The expected response code.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Retrieves the custom headers.
	 * 
	 * @return The custom headers.
	 */
	public Headers getHeaders() {
		return headers;
	}
	
	/**
	 * Modifies the response encoding type.
	 * 
	 * @param responseEncType The new response encoding type.
	 * 
	 * @return The request.
	 */
	public Request setResponseEncType(EncType responseEncType) {
		this.responseEncType = responseEncType;
		return this;
	}
	
	/**
	 * Retrieves the expected response encoding type.
	 * 
	 * @return The encoding type.
	 */
	public EncType getResponseEncType() {
		return responseEncType;
	}
	
	/**
	 * Modifies whether the request is an XML HTTP request or not.
	 * 
	 * @param xmlHttpRequest The next state.
	 * 
	 * @return The request.
	 */
	public Request setXMLHttpRequest(boolean xmlHttpRequest) {
		this.xmlHttpRequest = xmlHttpRequest;
		return this;
	}
	
	/**
	 * Retrieves whether the request is an XML HTTP request or not.
	 * 
	 * @return The result.
	 */
	public boolean isXMLHttpRequest() {
		return xmlHttpRequest;
	}
	
	/**
	 * Modifies the port.
	 * 
	 * @param port The new port.
	 * 
	 * @return The request.
	 */
	public Request setPort(int port) {
		this.port = port;
		return this;
	}
	
	/**
	 * Retrieves the port.
	 * 
	 * @return The port.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Modifies the attempts.
	 * 
	 * @param attempts The new attempts.
	 */
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	/**
	 * Retrieves the attempts.
	 * 
	 * @return The attempts.
	 */
	public int getAttempts() {
		return attempts;
	}
	
	/**
	 * Modifies the save path.
	 * 
	 * @param savePath The new save path.
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	/**
	 * Retrieves the save path.
	 * 
	 * @return The save path.
	 */
	public String getSavePath() {
		return savePath;
	}
	
	/**
	 * Modifies the cache control.
	 * 
	 * @param cacheControl The new cache control.
	 * 
	 * @return The request.
	 */
	public Request setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
		return this;
	}
	
	/**
	 * Retrieves the cache control.
	 * 
	 * @return The cache control.
	 */
	public String getCacheControl() {
		return cacheControl;
	}
	
	/**
	 * Assigns a progress listener to the request.
	 * 
	 * @return The request.
	 */
	public Request setProgressListener(RequestProgressListener progressListener) {
		this.progressListener = progressListener;
		return this;
	}
	
	/**
	 * Retrieves the progress listener if any.
	 * 
	 * @return The progress listener.
	 */
	public RequestProgressListener getProgressListener() {
		return progressListener;
	}
	
	/**
	 * Modifies whether to decode the body or not.
	 * 
	 * @param decodeBody The new state.
	 * 
	 * @return The request.
	 */
	public Request setDecodeBody(boolean decodeBody) {
		this.decodeBody = decodeBody;
		return this;
	}
	
	/**
	 * Retrieves whether to decode the body or not.
	 * 
	 * @return The result.
	 */
	public boolean isDecodeBody() {
		return decodeBody;
	}
	
	/**
	 * Retrieves the data counter.
	 * 
	 * @return The data counter.
	 */
	public DataCounter getDataCounter() {
		return dataCounter;
	}

}
