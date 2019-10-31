package com.nattguld.http.content.bodies;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.nattguld.http.content.ContentBody;
import com.nattguld.http.content.EncType;
import com.nattguld.http.stream.HTTPOutputStream;
import com.nattguld.util.generics.kvps.impl.AttributeKeyValuePair;

/**
 * 
 * @author randqm
 *
 */

public class FormBody extends ContentBody<List<AttributeKeyValuePair>> {
	
	/**
	 * The key-value pairs.
	 */
	private final List<AttributeKeyValuePair> kvps;

	
	/**
	 * Creates a new form body.
	 */
	public FormBody() {
		super(EncType.URL_ENCODED);
		
		this.kvps = new LinkedList<>();
	}
	
	/**
	 * Adds a new key-value pair.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 * 
	 * @return The body.
	 */
	public FormBody add(String key, Object value) {
		return add(key, value, false);
	}
	
	/**
	 * Adds a new key-value pair.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 * 
	 * @param noReplace Whether to keep existing entries with the same key.
	 * 
	 * @return The body.
	 */
	public FormBody add(String key, Object value, boolean noReplace) {
		if (!noReplace) {
			AttributeKeyValuePair exists = null;
			
			for (AttributeKeyValuePair kvp : kvps) {
				if (kvp.getKey().equals(key)) {
					exists = kvp;
					break;
				}
			}
			if (Objects.nonNull(exists)) {
				kvps.remove(exists);
			}
		}
		kvps.add(new AttributeKeyValuePair(key, value instanceof String ? (String)value : String.valueOf(value)));
		return this;
	}

	@Override
	protected void build(HTTPOutputStream httpStream, boolean prepare) throws IOException {
		int index = 0;
		
		for (AttributeKeyValuePair kvp : kvps) {
			String value = new String(kvp.getValueAsString().getBytes(StandardCharsets.UTF_8));
				
			if (!kvp.getKey().isEmpty()) {
				httpStream.writeString(kvp.getKey() + "=" + value);
			}
			if (++index < kvps.size()) {
				httpStream.writeString("&");
			}
		}
	}

	@Override
	public List<AttributeKeyValuePair> getContent() {
		return kvps;
	}

}