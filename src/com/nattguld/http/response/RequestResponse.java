package com.nattguld.http.response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import com.nattguld.data.ResourceIO;
import com.nattguld.data.json.JsonReader;
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
	 * The expected response code.
	 */
	private final int expectedCode;
	
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
     * The response json reader.
     */
    private JsonReader jsonReader;
	
	
    /**
     * Creates a new HTTP response.
     * 
     * @param endpoint The endpoint url.
     * 
     * @param expectedCode The expected response code.
     * 
     * @param responseStatus The response status.
     * 
     * @param responseBody The response body.
     * 
     * @param headers The response headers.
     */
	public RequestResponse(String endpoint, int expectedCode, ResponseStatus responseStatus, IResponseBody<?> responseBody, Headers headers) {
		this.endpoint = endpoint;
		this.expectedCode = expectedCode;
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
    	return validate(expectedCode);
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
		return responseStatus.getHttpCode().getCode();
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
	 * Parses a document to the response.
	 * 
	 * @param html The html content.
	 * 
	 * @return The parsed document.
	 */
	public Document parseDocument(String html) {
		String baseUri = NetUtil.getBaseUrl(endpoint);
		
		try (InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"))) {
    		doc = Jsoup.parse(in, "UTF-8", baseUri, Parser.htmlParser());
    		
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    		try {
    			doc = Jsoup.parse(new String(html.getBytes(), "UTF-8"), baseUri);
    			doc.outputSettings().charset("UTF-8");
    			
    		} catch (UnsupportedEncodingException ex2) {
    			ex2.printStackTrace();
				doc = Jsoup.parse(html, baseUri);
			}
    	}
		return doc;
	}
	
    /**
     * Retrieves the response content as HTML document.
     * 
     * @return The document.
     */
    public Document getAsDoc() {
    	if (Objects.isNull(doc)) {
    		doc = parseDocument(getResponseContent().contains("<html>") 
        			? getResponseContent() : ("<html><head></head><body>" + getResponseContent() + "</body></html>"));
    	}
    	return doc;
    }
    
    /**
     * Assigns a json reader as response.
     * 
     * @param jsonReader The json reader.
     * 
     * @return The request.
     */
    public RequestResponse setJsonReader(JsonReader jsonReader) {
    	this.jsonReader = jsonReader;
    	return this;
    }
    
    /**
     * Retrieves the response content as json element.
     * 
     * @return The json element.
     */
    public JsonReader getJsonReader() {
    	return getJsonReader(false);
    }
    
    /**
     * Retrieves the response content as json element.
     * 
     * @return The json element.
     */
    public JsonReader getJsonReader(boolean disableHtmlEscaping) {
    	if (Objects.isNull(jsonReader)) {
    		jsonReader = ResourceIO.loadJsonFromString(getResponseContent(), disableHtmlEscaping);
    	}
    	return jsonReader;
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
