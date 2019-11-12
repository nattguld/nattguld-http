package com.nattguld.http.response.interpretors;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nattguld.http.response.ResponseInterpretor;
import com.nattguld.http.response.bodies.impl.FileResponseBody;

/**
 * 
 * @author randqm
 *
 */

public class FileInterpretor extends ResponseInterpretor<FileResponseBody> {
	
	/**
	 * The save path.
	 */
	private final String savePath;
	
	
	/**
	 * Creates a new file decoder.
	 * 
	 * @param bodySize The expected body size.
	 * 
	 * @param savePath The save path.
	 */
	public FileInterpretor(int bodySize, String savePath) {
		super(bodySize);
		
		this.savePath = savePath;
	}
	
	@Override
	public FileResponseBody interpret(BufferedInputStream bis) throws IOException {
		int read = 0;
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
			byte dataBuffer[] = new byte[4096];
			int bytesRead = 0;
			
			while ((bytesRead = bis.read(dataBuffer, 0, 4096)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
				read += bytesRead;
				
				addProgress(bytesRead);
				
				if (read == getBodySize()) {
					break;
				}
			}
			return new FileResponseBody(new File(savePath));
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Exception occurred while decoding file");
		}
		return null;
	}
 
}
