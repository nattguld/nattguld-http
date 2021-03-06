package com.nattguld.http.response.decode.impl;

import java.io.IOException;
import java.io.InputStream;

import org.brotli.dec.BrotliInputStream;

import com.nattguld.http.response.decode.IResponseDecoder;

/**
 * 
 * @author randqm
 *
 */

public class BrotliDecoder implements IResponseDecoder {


	@Override
	public InputStream decode(InputStream in) throws IOException {
		return new BrotliInputStream(in);
	}

}
