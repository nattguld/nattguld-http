package com.nattguld.http;

/**
 * 
 * @author randqm
 *
 */

public enum HTTPCode {
	
	INVALID("Invalid", 0, "Invalid"),
	UNKNOWN("Unknown", 0, "Unknown"),
	
	//Informational
	CONTINUE("Continue", 100, "The server, has received the request headers and the client should proceed to send the request body"),
	SWITCHING_PROTOCOLS("Switching Protocols", 101, "The requester has asked the server to switch protocols and the server has agreed to do so"),
	PROCESSING("Processing", 102, "The server has received and is processing the request, but no response is available yet"),
	EARLY_HINTS("Early Hints", 103, "Partial response received, still waiting for final message"),
	
	//Success
	OK("OK", 200, "Standard response for successful HTTP requests"),
	CREATED("Created", 201, "The request has been fulfilled, resulting in the creation of a new resource"),
	ACCEPTED("Accepted", 202, "The request has been accepted for processing, but the processing has not been completed"),
	NON_AUTHORITATIVE_INFORMATION("Non-Authoritative Information", 203, "The server is a transforming proxy that received a 200 OK from its origin, but is returning a modified version of the origin's response"),
	NO_CONTENT("No Content", 204, "The server successfully processed the request and is not returning any content"),
	RESET_CONTENT("Reset Content", 205, "The server successfully processed the request, but is not returning any content, reset the document view"),
	PARTIAL_CONTENT("Partial Content", 206, "The server is delivering only part of the resource due to a range header sent by the client"),
	MULTI_STATUS("Multi-Status", 207, "The message body that follows is by default an XML message and can contain a number of separate response codes, depending on how many sub-requests were made"),
	ALREADY_REPORTED("Already Reported", 208, "The members of a DAV binding have already been enumerated in a preceding part of the (multistatus) response, and are not being included again"),
	IM_USED("IM Used", 226, "The server has fulfilled a request for the resource, and the response is a representation of the result of one or more instance-manipulations applied to the current instance"),
	
	//Redirection
	MULTIPLE_CHOICES("Multiple Choices", 300, "Indicates multiple options for the resource from which the client may choose"),
	MOVED_PERMANENTLY("Moved Permanently", 301, "This and all future requests should be directed to the given URI"),
	FOUND("Found", 302, "Tells the client to look at (browse to) another URL"),
	SEE_OTHER("See Other", 303, "The response to the request can be found under another URI using the GET method"),
	NOT_MODIFIED("Not Modified", 304, "Indicates that the resource has not been modified since the version specified by the request headers If-Modified-Since or If-None-Match"),
	TEMPORARY_REDIRECT("Temporary Redirect", 307, "The request should be repeated with another URI; however, future requests should still use the original URI"),
	PERMANENT_REDIRECT("Permanent Redirect", 308, "The request and all future requests should be repeated using another URI"),
	
	//Client error
	BAD_REQUEST("Bad Request", 400, "The server cannot or will not process the request due to an apparent client error"),
	UNAUTHORIZED("Unauthorized", 401, "Authentication is required and has failed or has not yet been provided"),
	FORBIDDEN("Forbidden", 403, "The request contained valid data and was understood by the server, but the server is refusing action"),
	NOT_FOUND("Not Found", 404, "The requested resource could not be found but may be available in the future"),
	METHOD_NOT_ALLOWED("Method Not Allowed", 405, "A request method is not supported for the requested resource"),
	NOT_ACCEPTABLE("Not Acceptable", 406, "The requested resource is capable of generating only content not acceptable according to the Accept headers sent in the request"),
	PROXY_AUTHENTICATION_REQUIRED("Proxy Authentication Required", 407, "The client must first authenticate itself with the proxy"),
	REQUEST_TIMEOUT("Request Timeout", 408, "The server timed out waiting for the request"),
	CONFLICT("Conflict", 409, "Indicates that the request could not be processed because of conflict in the current state of the resource, such as an edit conflict between multiple simultaneous updates"),
	GONE("Gone", 410, "Indicates that the resource requested is no longer available and will not be available again"),
	LENGTH_REQUIRED("Length Required", 411, "The request did not specify the length of its content, which is required by the requested resource"),
	PRECONDITION_FAILED("Precondition Failed", 412, "The server does not meet one of the preconditions that the requester put on the request header fields"),
	PAYLOAD_TOO_LARGE("Payload Too Large", 413, "The request is larger than the server is willing or able to process"),
	URI_TOO_LONG("URI Too Long", 414, "The URI provided was too long for the server to process"),
	UNSUPPORTED_MEDIA_TYPE("Unsupported Media Type", 415, "The request entity has a media type which the server or resource does not support"),
	RANGE_NOT_SATISFIABLE("Range Not Satisfiable", 416, "The client has asked for a portion of the file, but the server cannot supply that portion"),
	EXCEPTION_FAILED("Expectation Failed", 417, "The server cannot meet the requirements of the Expect request-header field"),
	MISDIRECTED_REQUEST("Misdirected Request", 421, "The request was directed at a server that is not able to produce a response"),
	UNPROCESSABLE_ENTITY("Unprocessable Entity", 422, "The request was well-formed but was unable to be followed due to semantic errors"),
	LOCKED("Locked", 423, "The resource that is being accessed is locked"),
	FAILED_DEPENDENCY("Failed Dependency", 424, "The request failed because it depended on another request and that request failed"),
	TOO_EARLY("Too Early", 425, "Indicates that the server is unwilling to risk processing a request that might be replayed"),
	UPGRADE_REQUIRED("Upgrade Required", 426, "The client should switch to a different protocol such as TLS/1.0, given in the Upgrade header field"),
	PRECONDITION_REQUIRED("Precondition Required", 428, "The origin server requires the request to be conditional"),
	TOO_MANY_REQUESTS("Too Many Requests", 429, "The user has sent too many requests in a given amount of time"),
	REQUEST_HEADER_FIELDS_TOO_LARGE("Request Header Fields Too Large", 431, "The server is unwilling to process the request because either an individual header field, or all the header fields collectively, are too large"),
	UNAVAILABLE_FOR_LEGAL_REASONS("Unavailable For Legal Reasons", 451, "A server operator has received a legal demand to deny access to a resource or to a set of resources that includes the requested resource"),
	
	//Server error
	INTERNAL_SERVER_ERROR("Internal Server Error", 500, "A generic error message, given when an unexpected condition was encountered and no more specific message is suitable"),
	NOT_IMPLEMENTED("Not Implemented", 501, "The server either does not recognize the request method, or it lacks the ability to fulfil the request"),
	BAD_GATEWAY("Bad Gateway", 502, "The server was acting as a gateway or proxy and received an invalid response from the upstream server"),
	SERVICE_UNAVAILABLE("Service Unavailable", 503, "The server cannot handle the request (because it is overloaded or down for maintenance)"),
	GATEWAY_TIMEOUT("Gateway Timeout", 504, "The server was acting as a gateway or proxy and did not receive a timely response from the upstream server"),
	HTTP_VERSION_NOT_SUPPORTED("HTTP Version Not Supported", 505, "The server does not support the HTTP protocol version used in the request"),
	VARIANT_ALSO_NEGOTIATES("Variant Also Negotiates", 506, "Transparent content negotiation for the request results in a circular reference"),
	INSUFFICIENT_STORAGE("Insufficient Storage", 507, "The server is unable to store the representation needed to complete the request"),
	LOOP_DETECTED("Loop Detected", 508, "The server detected an infinite loop while processing the request"),
	NOT_EXTENDED("Not Extended", 510, "Further extensions to the request are required for the server to fulfil it"),
	NETWORK_AUTHENTICATION_REQUIRED("Network Authentication Required", 511, "The client needs to authenticate to gain network access"),
	;
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The code.
	 */
	private final int code;
	
	/**
	 * The message.
	 */
	private final String message;
	
	
	/**
	 * Creates a new HTTP code.
	 * 
	 * @param name The name.
	 * 
	 * @param code The code.
	 * 
	 * @param message The messsage.
	 */
	private HTTPCode(String name, int code, String message) {
		this.name = name;
		this.code = code;
		this.message = message;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the code.
	 * 
	 * @return The code.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Retrieves the message.
	 * 
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Retrieves whether the HTTP code is information or not.
	 * 
	 * @return The result.
	 */
	public boolean isInformational() {
		return code >= 100 && code < 200;
	}
	
	/**
	 * Retrieves whether the HTTP code is a success code or not.
	 * 
	 * @return The result.
	 */
	public boolean isSuccess() {
		return code >= 200 && code < 300;
	}
	
	/**
	 * Retrieves whether the HTTP code is a redirection code or not.
	 * 
	 * @return The result.
	 */
	public boolean isRedirection() {
		return code >= 300 && code < 400;
	}
	
	/**
	 * Retrieves whether the HTTP code is a client error code or not.
	 * 
	 * @return The result.
	 */
	public boolean isClientError() {
		return code >= 400 && code < 500;
	}
	
	/**
	 * Retrieves whether the HTTP code is a server error code or not.
	 * 
	 * @return The result.
	 */
	public boolean isServerError() {
		return code >= 500;
	}
	
	@Override
	public String toString() {
		return "[" + getCode() + "][" + getName() + "]: " + getMessage();
	}
	
	/**
	 * Retrieves a HTTP code for a given code.
	 * 
	 * @param code The code.
	 * 
	 * @return The HTTP code.
	 */
	public static HTTPCode getForCode(int code) {
		for (HTTPCode httpCode : HTTPCode.values()) {
			if (httpCode.getCode() == code) {
				return httpCode;
			}
		}
		return HTTPCode.INVALID;
	}

}
