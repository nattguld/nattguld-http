package com.nattguld.http.content.bodies;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.nattguld.http.content.ContentBody;
import com.nattguld.http.content.EncType;
import com.nattguld.http.stream.HTTPOutputStream;

/**
 * 
 * @author randqm
 *
 */

public class StringBody extends ContentBody<String> {
	
	/**
	 * The content.
	 */
	private final String content;
	
	
	/**
	 * Creates a new string body.
	 * 
	 * @param encType The encoding type.
	 * 
	 * @param content The content.
	 */
	public StringBody(EncType encType, String content) {
		super(encType);
		
		this.content = content;
	}

	@Override
	protected void build(HTTPOutputStream httpStream, boolean prepare) throws IOException {
		httpStream.writeString(new String(content.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8));
	}
	
	/**
	 * Retrieves the content.
	 * 
	 * @return The content.
	 */
	public String getContent() {
		return content;
	}

}
