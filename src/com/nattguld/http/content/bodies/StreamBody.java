package com.nattguld.http.content.bodies;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.ContentBody;
import com.nattguld.http.content.EncType;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.stream.HTTPOutputStream;
import com.nattguld.util.files.MimeType;

/**
 * 
 * @author randqm
 *
 */

public class StreamBody extends ContentBody<File> {

	/**
	 * The file.
	 */
	private final File file;
	
	/**
	 * Whether it's a raw stream or not.
	 */
	private final boolean raw;

	
	/**
	 * Creates a new stream body.
	 * 
	 * @param file The file.
	 * 
	 * @param raw Whether it's a raw stream or not.
	 */
	public StreamBody(File file, boolean raw) {
		super(EncType.STREAM);
		
		this.file = file;
		this.raw = raw;
	}
	
	@Override
	protected void build(HTTPOutputStream httpStream, boolean prepare) throws IOException {
		if (prepare) {
			return;
		}
		try (FileInputStream fis = new FileInputStream(file)) {
			int bytesRead = -1;
			byte[] buffer = new byte[NetConfig.getConfig().getChunkSize()];
    	
			while ((bytesRead = fis.read(buffer)) != -1) {
				httpStream.write(buffer, 0, bytesRead);
			}
			httpStream.flush();
		}
	}
	
	@Override
	protected void setContentLength(int contentLength) {
		super.setContentLength(contentLength + (int)file.length());
	}
	
	@Override
	protected void setContentHeaders(Headers headers) {
		headers.add("Content-Type", raw ? getEncType().getContentTypeHeader() : MimeType.getByFile(file).getName());
		headers.add("Content-Length", Integer.toString(getContentLength()));
	}

	@Override
	public File getContent() {
		return file;
	}

}
