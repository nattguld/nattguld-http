package com.nattguld.http.stream;

import java.io.IOException;
import java.io.OutputStream;

import com.nattguld.http.DataCounter;

/**
 * 
 * @author randqm
 *
 */

public class CountOutputStream extends OutputStream implements ICountedStream {
	
	/**
	 * The output stream.
	 */
	private final OutputStream out;

	/**
	 * The data counter.
	 */
	private final DataCounter dc;
	
	
	/**
	 * Creates a new counted output stream.
	 * 
	 * @param out The output stream.
	 * 
	 * @param dc The data counter.
	 */
	public CountOutputStream(OutputStream out, DataCounter dc) {
		this.out = out;
		this.dc = dc;
	}
	
	@Override
	public void write(byte[] buffer) throws IOException {
		out.write(buffer);
		dc.addUp(buffer.length);
	}

	@Override
	public void write(byte[] buffer, int off, int len) throws IOException {
		out.write(buffer, off, len);
		dc.addUp(len);
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
		dc.addUp(1);
	}
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}
	
	@Override
	public void close() throws IOException {
		out.close();
	}
	
	@Override
	public DataCounter getDataCounter() {
		return dc;
	}

}
