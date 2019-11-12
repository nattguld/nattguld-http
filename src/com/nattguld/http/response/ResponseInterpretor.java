package com.nattguld.http.response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.nattguld.http.response.bodies.IResponseBody;

/**
 * 
 * @author randqm
 *
 * @param <T>
 */

public abstract class ResponseInterpretor<T extends IResponseBody<? extends Object>> {
	
	/**
	 * The interpretation progress.
	 */
	private final AtomicInteger progress;
	
	/**
	 * The body size.
	 */
	private int bodySize;
	
	
	/**
	 * Creates a new response interpretor.
	 * 
	 * @param bodySize The body size.
	 */
	public ResponseInterpretor(int bodySize) {
		this.progress = new AtomicInteger();
		this.bodySize = bodySize;
	}
	
	/**
	 * Interprets the response.
	 * 
	 * @param in The input stream.
	 * 
	 * @return The response body.
	 * 
	 * @exception IOException
	 */
	public abstract T interpret(BufferedInputStream bis) throws IOException;

	
	/**
	 * Modifies the progress.
	 * 
	 * @param progress The new progress.
	 */
	protected void addProgress(int progress) {
		this.progress.addAndGet(progress);
	}
	
	/**
	 * Retrieves the decoded body size.
	 * 
	 * @return The decoded body size.
	 */
	protected int getBodySize() {
		return bodySize;
	}
	
	/**
	 * Retrieves the interpretation progress.
	 * 
	 * @return The progress.
	 */
	public int getProgress() {
		return (int)Math.round(((double)((double)progress.get() / (double)getBodySize() * 100)));
	}
	
}
