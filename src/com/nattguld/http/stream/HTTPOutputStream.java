package com.nattguld.http.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author randqm
 *
 */

public class HTTPOutputStream extends ByteArrayOutputStream {
	
	/**
	 * The line feed string.
	 */
	protected static final String LINE_FEED = "\r\n";

	
	/**
	 * Writes a line.
	 * 
	 * @param line The line.
	 * 
	 * @return The stream.
	 * 
	 * @throws IOException 
	 * 
	 * @throws UnsupportedEncodingException 
	 */
	public HTTPOutputStream writeLine(String line) throws UnsupportedEncodingException, IOException {
		writeString(line);
		writeLine();
		return this;
	}
	
	/**
	 * Writes an empty line.
	 * 
	 * @return The stream.
	 * 
	 * @throws IOException 
	 * 
	 * @throws UnsupportedEncodingException 
	 */
	public HTTPOutputStream writeLine() throws UnsupportedEncodingException, IOException {
		return writeString(LINE_FEED);
	}
	
	/**
	 * Writes a string.
	 * 
	 * @param s The string.
	 * 
	 * @return The stream.
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @throws IOException
	 */
	public HTTPOutputStream writeString(String s) throws UnsupportedEncodingException, IOException {
		return writeString(s, "UTF-8");
	}
	
	/**
	 * Writes a string.
	 * 
	 * @param s The string.
	 * 
	 * @param charSet The character set.
	 * 
	 * @return The stream.
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @throws IOException
	 */
	public HTTPOutputStream writeString(String s, String charSet) throws UnsupportedEncodingException, IOException {
		write(s.getBytes(charSet));
		return this;
	}

}
