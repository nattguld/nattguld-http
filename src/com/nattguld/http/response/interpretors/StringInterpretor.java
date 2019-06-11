package com.nattguld.http.response.interpretors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.response.ResponseInterpretor;
import com.nattguld.http.response.bodies.impl.StringResponseBody;
import com.nattguld.http.response.decode.IResponseDecoder;
import com.nattguld.http.response.decode.impl.BrotliDecoder;
import com.nattguld.http.response.decode.impl.DeflateDecoder;
import com.nattguld.http.response.decode.impl.GzipDecoder;

/**
 * 
 * @author randqm
 *
 */

public class StringInterpretor extends ResponseInterpretor<StringResponseBody> {
	
	/**
	 * Holds the available response decoders.
	 */
	private static Map<String, IResponseDecoder> decoders = new HashMap<>();
	
	/**
	 * Loads the available response decoders into a static manner.
	 */
	static {
		decoders.put("gzip", new GzipDecoder());
		decoders.put("br", new BrotliDecoder());
		decoders.put("deflate", new DeflateDecoder());
	}

	/**
	 * The content encoding.
	 */
	private final String contentEncoding;
	
	/**
	 * The content type.
	 */
	private final String contentType;
	
	
	/**
	 * Creates a new string decoder.
	 * 
	 * @param bodySize The body size.
	 * 
	 * @param contentEncoding The content encoding.
	 * 
	 * @param contentType The content type.
	 */
	public StringInterpretor(int bodySize, String contentEncoding, String contentType) {
		super(bodySize);
		
		this.contentEncoding = contentEncoding;
		this.contentType = contentType;
	}

	@Override
	public StringResponseBody interpret(InputStream in) throws IOException {
		IResponseDecoder decoder = Objects.nonNull(contentEncoding) ? decoders.get(contentEncoding) : null;

		if (NetConfig.getGlobalInstance().isDebug()) {
			System.out.println("Interpret decoder " + decoder + " for content type " + contentType);
		}
		StringBuilder sb = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.isNull(decoder) ? in : decoder.decode(in), "UTF-8"))) {
			byte b = 0;
	
			while ((b = (byte)br.read()) != -1) {
				sb.append((char)b);
				addProgress(1);
				
				if (Objects.isNull(contentEncoding) && sb.length() >= getBodySize()) {
					break;
				}
			}
		}
		if (NetConfig.getGlobalInstance().isDebug()) {
			System.out.println("Body interpretation successfull");
		}
		return new StringResponseBody(sb.toString());
	}

}
