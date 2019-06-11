package com.nattguld.http.response.decode.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import com.nattguld.http.response.decode.IResponseDecoder;

/**
 * 
 * @author randqm
 *
 */

public class DeflateDecoder implements IResponseDecoder {


	@Override
	public InputStream decode(InputStream in) throws IOException {
		return new InflaterInputStream(in);
	}

}
