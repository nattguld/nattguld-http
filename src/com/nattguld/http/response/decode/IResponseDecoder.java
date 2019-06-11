package com.nattguld.http.response.decode;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author randqm
 * 
 * @param <V>
 *
 */

public interface IResponseDecoder {
	
	
	/**
	 * Decodes an input stream.
	 * 
	 * @param in The input stream.
	 * 
	 * @return The decoded input stream.
	 * 
	 * @exception IOException
	 */
	public InputStream decode(InputStream in) throws IOException;

}
