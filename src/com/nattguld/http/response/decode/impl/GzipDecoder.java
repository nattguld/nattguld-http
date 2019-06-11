package com.nattguld.http.response.decode.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import com.nattguld.http.response.decode.IResponseDecoder;

/**
 * 
 * @author randqm
 *
 */

public class GzipDecoder implements IResponseDecoder {


	@Override
	public InputStream decode(InputStream in) throws IOException {
		return new GZIPInputStream(in);
	}

}
