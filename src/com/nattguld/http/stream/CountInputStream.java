package com.nattguld.http.stream;

import java.io.IOException;
import java.io.InputStream;

import com.nattguld.http.DataCounter;

/**
 * 
 * @author randqm
 *
 */

public class CountInputStream extends InputStream implements ICountedStream {
	
	/**
	 * The input stream.
	 */
	private final InputStream in;
	
	/**
	 * The data counter.
	 */
	private final DataCounter dc;
	
	/**
	 * The marked input.
	 */
	private long marked;
	
	
	/**
	 * Creates a new count input stream.
	 * 
	 * @param in The input stream.
	 * 
	 * @param dc The data counter.
	 */
	public CountInputStream(InputStream in, DataCounter dc) {
		this.in = in;
		this.dc = dc;
		this.marked = -1;
	}

	@Override
	public int read() throws IOException {
		int b = in.read();
		
		if (b > 0) {
			dc.addDown(1);
		}
	    return b;
	}
	
	@Override
	public int read(byte[] buffer, int off, int len) throws IOException {
		int b = in.read(buffer, off, len);
	    
		if (b > 0) {
			dc.addDown(b);
		}
		return b;
	}

	@Override
	public long skip(long skipped) throws IOException {
	    long l = in.skip(skipped);
	    
	    if (l > 0) {
	    	dc.addDown(l);
	    }
	    return l;
	}
	
	@Override
	public void mark(int readlimit) {
		in.mark(readlimit);
		marked = dc.getDown();
	}
	
	@Override
	public void reset() throws IOException {
		in.reset();
		dc.setDown(marked);
	}
	
	@Override
	public void close() throws IOException {
		in.close();
	}
	
	@Override
	public boolean markSupported() {
		return in.markSupported();
	}
	  
	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
	public DataCounter getDataCounter() {
		return dc;
	}

}
