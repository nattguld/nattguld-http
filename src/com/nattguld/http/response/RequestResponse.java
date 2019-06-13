package com.nattguld.http.response;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.response.bodies.IResponseBody;
import com.nattguld.http.response.bodies.impl.StringResponseBody;
import com.nattguld.http.util.NetUtil;

/**
 * 
 * @author randqm
 *
 */

public class RequestResponse {
	
	/**
	 * The endpoint url.
	 */
	private final String endpoint;
	
	/**
	 * The response status.
	 */
	private final ResponseStatus responseStatus;
	
	/**
	 * The response body.
	 */
	private final IResponseBody<?> responseBody;
	
	/**
	 * The response headers.
	 */
	private final Headers headers;
	
    /**
     * The response content as document.
     */
    private Document doc;
    
    /**
     * The response content as json element.
     */
    private JsonElement jsonEl;
	
	
    /**
     * Creates a new HTTP response.
     * 
     * @param endpoint The endpoint url.
     * 
     * @param responseStatus The response status.
     * 
     * @param responseBody The response body.
     * 
     * @param headers The response headers.
     */
	public RequestResponse(String endpoint, ResponseStatus responseStatus, IResponseBody<?> responseBody, Headers headers) {
		this.endpoint = endpoint;
		this.responseStatus = responseStatus;
		this.responseBody = responseBody;
		this.headers = headers;
	}
	
	/**
	 * Retrieves the endpoint url.
	 * 
	 * @return The endpoint url.
	 */
	public String getEndpoint() {
		return endpoint;
	}
	
    /**
     * Validates the response.
     * 
     * @return The validation result.
     */
    public boolean validate() {
    	return validate(200);
    }
    
    /**
     * Validates the response.
     * 
     * @param expectedCode The expected response code.
     * 
     * @return The validation result.
     */
    public boolean validate(int expectedCode) {
    	return getCode() == expectedCode;
    }
	
	/**
	 * Retrieves the response code.
	 * 
	 * @return The response code.
	 */
	public int getCode() {
		return responseStatus.getCode();
	}
	
	/**
	 * Retrieves the response status.
	 * 
	 * @return The response status.
	 */
	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}
	
	/**
	 * Retrieves the response body.
	 * 
	 * @return The response body.
	 */
	public IResponseBody<?> getResponseBody() {
		return responseBody;
	}
	
	/**
	 * Retrieves the response content.
	 * 
	 * @return The response content.
	 */
	public String getResponseContent() {
		if (!(getResponseBody() instanceof StringResponseBody)) {
			try {
				throw new Exception("Unable to fetch response content from a non-StringResponseBody");
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ((StringResponseBody)getResponseBody()).getBody();
	}
	
	/**
	 * Retrieves the headers.
	 * 
	 * @return The headers.
	 */
	public Headers getHeaders() {
		return headers;
	}
	
    /**
     * Retrieves the response content as HTML document.
     * 
     * @return The document.
     */
    public Document getAsDoc() {
    	if (Objects.isNull(doc)) {
    		String baseUri = NetUtil.getBaseUrl(endpoint);
    		String content = getResponseContent().contains("<html>") 
        			? getResponseContent() : ("<html><head></head><body>" + getResponseContent() + "</body></html>");
        			
    		try {
				doc = Jsoup.parse(new String(content.getBytes(), "UTF-8"), baseUri);
				doc.outputSettings().charset("UTF-8");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
				doc = Jsoup.parse(content, baseUri);
			}
    	}
    	return doc;
    }
    
    /**
     * Retrieves the response content as json element.
     * 
     * @return The json element.
     */
    public JsonElement getAsJsonElement() {
    	if (Objects.isNull(jsonEl)) {
    		jsonEl = new JsonParser().parse(getResponseContent());
    	}
    	return jsonEl;
    }
    
    /**
     * Retrieves the final location.
     * 
     * @return The final location.
     */
    public String getLocation() {
    	return getHeaders().getValueIgnoreCase("Location");
    }
    
    @Override
    public String toString() {
    	return "[endpoint: " + endpoint + "][status: " + responseStatus + "]";
    }

}
