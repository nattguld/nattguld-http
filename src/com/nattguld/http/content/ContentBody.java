package com.nattguld.http.content;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.nattguld.http.headers.Headers;
import com.nattguld.http.stream.HTTPOutputStream;

/**
 * 
 * @author randqm
 *
 */

public abstract class ContentBody<T> {
	
	/**
	 * The encoding type.
	 */
	private final EncType encType;
	
	/**
	 * The content length.
	 */
	private int contentLength;
	
	/**
	 * Whether the body is supposed to be sent chunked or not.
	 */
	private boolean chunked;
	
	/**
	 * The chunk stream.
	 */
	private ChunkHandler chunkHandler;
	
	
	/**
	 * Creates a new content body.
	 * 
	 * @param encType The encoding type.
	 */
	public ContentBody(EncType encType) {
		this.encType = encType;
	}
	
	/**
	 * Builds the content body.
	 * 
	 * @param httpStream The HTTP output stream.
	 * 
	 * @param prepare Whether it's a preparation or write build.
	 * 
	 * @throws IOException
	 */
	protected abstract void build(HTTPOutputStream httpStream, boolean prepare) throws IOException;
	
	/**
	 * Prepares the content body for write.
	 * 
	 * @param headers The current request headers.
	 * 
	 * @param chunked Whether the body should be sent chunked or not.
	 * 
	 * @return The content body.
	 */
	public ContentBody<T> prepare(Headers headers) {
		try (HTTPOutputStream httpStream = new HTTPOutputStream()) {
			try (OutputStreamWriter osw = new OutputStreamWriter(httpStream, Charset.forName("UTF-8").newEncoder())) {
				build(httpStream, true);
			}
			setContentLength(httpStream.size());
			setContentHeaders(headers);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Exception while preparing content body");
		}
		return this;
	}
	
	/**
	 * Writes the content body.
	 * 
	 * @param out The buffered output stream.
	 * 
	 * @return The content body.
	 */
	public ContentBody<T> write(OutputStream out) {
		try (HTTPOutputStream httpStream = new HTTPOutputStream()) {
			try (OutputStreamWriter osw = new OutputStreamWriter(httpStream, Charset.forName("UTF-8").newEncoder())) {
				build(httpStream, false);
			}
			httpStream.writeTo(out);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Exception while writing content body");
		}
		return this;
	}
	
	/**
	 * Modifies the content headers.
	 * 
	 * @param headers The current headers.
	 */
	protected void setContentHeaders(Headers headers) {
		headers.add("Content-Type", getEncType().getContentTypeHeader());
		headers.add("Content-Length", Integer.toString(getContentLength()));
		//headers.add("Transfer-Encoding", "chunked");
	}
	
	/**
	 * Modifies whether the body should be sent chunked or not.
	 * 
	 * @param chunked The new state.
	 * 
	 * @return The content body.
	 */
	public ContentBody<?> setChunked(boolean chunked) {
		this.chunked = chunked;
		return this;
	}
	
	/**
	 * Retrieves whether the body should be sent chunked or not.
	 * 
	 * @return The result.
	 */
	public boolean isChunked() {
		return chunked;
	}
	
	/**
	 * Modifies the chunk handler.
	 * 
	 * @param chunkHandler The new chunk handler.
	 * 
	 * @return The body.
	 */
	public ContentBody<?> setChunkHandler(ChunkHandler chunkHandler) {
		this.chunkHandler = chunkHandler;
		return this;
	}
	
	/**
	 * Retrieves the chunk handler.
	 * 
	 * @return The chunk handler.
	 */
	public ChunkHandler getChunkHandler() {
		return chunkHandler;
	}
	
	/**
	 * Modifies the content length.
	 * 
	 * @param contentLength The new content length.
	 */
	protected void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	
	/**
	 * Retrieves the content length.
	 * 
	 * @return The content length.
	 */
	protected int getContentLength() {
		return contentLength;
	}
	
	/**
	 * Retrieves the encoding type.
	 * 
	 * @return The encoding type.
	 */
	public EncType getEncType() {
		return encType;
	}
	
	/**
	 * Retrieves the content.
	 * 
	 * @return The content.
	 */
	public abstract T getContent();

}
