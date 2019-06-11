package com.nattguld.http.response.bodies.impl;

import java.io.File;

import com.nattguld.http.response.bodies.IResponseBody;

/**
 * 
 * @author randqm
 *
 */

public class FileResponseBody implements IResponseBody<File> {

	/**
	 * The file.
	 */
	private final File file;
	
	
	/**
	 * Creates a new file body.
	 * 
	 * @param file The file.
	 */
	public FileResponseBody(File file) {
		this.file = file;
	}

	@Override
	public File getBody() {
		return file;
	}

}
