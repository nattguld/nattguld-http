package com.nattguld.http.content.bodies;

import java.io.IOException;
import java.io.OutputStream;

import com.nattguld.http.content.ContentBody;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.stream.HTTPOutputStream;

/**
 * 
 * @author randqm
 *
 */

public class EmptyBody extends ContentBody<Object> {

	
	/**
	 * Creates a new empty body.
	 */
	public EmptyBody() {
		super(null);
	}
	
	@Override
	public ContentBody<Object> prepare(Headers headers) {
		setContentLength(0);
		setContentHeaders(headers);
		return this;
	}

	@Override
	protected void build(HTTPOutputStream httpStream, boolean prepare) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ContentBody<Object> write(OutputStream out) {
		return this;
	}
	
	@Override
	protected void setContentHeaders(Headers headers) {
		headers.add("Content-Length", "0");
	}

	@Override
	public Object getContent() {
		return null;
	}

}
