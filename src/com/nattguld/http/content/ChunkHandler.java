package com.nattguld.http.content;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.stream.HTTPOutputStream;

/**
 * 
 * @author randqm
 *
 */

public class ChunkHandler implements AutoCloseable {
	
	/**
	 * The file to chunk.
	 */
	private final File file;
	
	/**
	 * The file size.
	 */
	private final long fileSize;
	
	/**
	 * The buffered file input stream.
	 */
	private final BufferedInputStream bis;
	
	/**
	 * The amount of chunks.
	 */
	private long chunks;
	
	/**
	 * The current chunk id.
	 */
	private int chunkId;
	
	/**
	 * The chunk size.
	 */
	private int chunkSize;
	
	/**
	 * The rest chunk size.
	 */
	private long restChunkSize;
	
	/**
	 * The start range.
	 */
	private int startRange;
	
	/**
	 * The end range.
	 */
	private long endRange;
	
	/**
	 * The amount of bytes sent.
	 */
	private int bytesSent;
	
	
	/**
	 * Creates a new chunked file handler.
	 * 
	 * @param file The file to chunk.
	 * 
	 * @throws FileNotFoundException 
	 */
	public ChunkHandler(File file) throws FileNotFoundException {
		this.file = file;
		this.fileSize = file.length();
		this.chunks = fileSize <= NetConfig.getConfig().getChunkSize() ? 1 
				: (fileSize / NetConfig.getConfig().getChunkSize()); 
		this.bis = new BufferedInputStream(new FileInputStream(file));
		
		if (chunks > 1) {
			restChunkSize = fileSize % (chunks * NetConfig.getConfig().getChunkSize());
			
			if (restChunkSize > 0) {
				chunks++;
			}
		}
	}
	
	/**
	 * Prepares the chunk to send.
	 */
	public void prepare() {
		chunkSize = NetConfig.getConfig().getChunkSize();
		startRange = chunkId * chunkSize;
		endRange = ((chunkId + 1) * chunkSize) - 1;
		
		if ((chunkId + 1) == chunks) {
			endRange = file.length() - 1;
			chunkSize = (int)restChunkSize;
		}
	}
	
	/**
	 * Writes a chunk.
	 * 
	 * @param out The output stream.
	 * 
	 * @throws IOException 
	 */
	public void writeChunk(HTTPOutputStream httpStream) throws IOException {
		byte[] buffer = new byte[chunkSize];
		bis.read(buffer);
		
		try {
			httpStream.write(buffer, 0, chunkSize);
		} catch (Exception ex) {
			httpStream.write(buffer, 0, chunkSize);
		}
		bytesSent += chunkSize;
		chunkId++;
	}
	
	@Override
	public void close() throws IOException {
		bis.close();
	}
	
	/**
	 * Retrieves the file being chunked.
	 * 
	 * @return The file.
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Retrieves the chunk size.
	 * 
	 * @return The chunk size.
	 */
	public int getChunkSize() {
		return chunkSize;
	}
	
	/**
	 * Retrieves the current start range.
	 * 
	 * @return The current start range.
	 */
	public int getStartRange() {
		return startRange;
	}
	
	/**
	 * Retrieves the current end range.
	 * 
	 * @return The end range.
	 */
	public long getEndRange() {
		return endRange;
	}
	
	/**
	 * Retrieves the transfer progress.
	 * 
	 * @return The transfer progress.
	 */
	public int getProgress() {
		return (int)Math.round((((double)bytesSent / (double)fileSize) * 100));
	}
	
	/**
	 * Retrieves whether the body has been fully transfered or not.
	 * 
	 * @return The result.
	 */
	public boolean isFinished() {
		return bytesSent >= fileSize;
	}

}
