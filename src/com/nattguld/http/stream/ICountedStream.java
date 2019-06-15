package com.nattguld.http.stream;

import com.nattguld.http.DataCounter;

/**
 * 
 * @author randqm
 *
 */

public interface ICountedStream {
	
	
	/**
	 * Retrieves the data counter.
	 * 
	 * @return The data counter.
	 */
	public DataCounter getDataCounter();
 
}
