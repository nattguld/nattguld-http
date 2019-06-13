package com.nattguld.http.content.bodies;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.content.ChunkHandler;
import com.nattguld.http.content.ContentBody;
import com.nattguld.http.content.EncType;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.stream.HTTPOutputStream;
import com.nattguld.util.files.MimeType;
import com.nattguld.util.generics.kvps.impl.AttributeKeyValuePair;
import com.nattguld.util.text.TextSeed;
import com.nattguld.util.text.TextUtil;

/**
 * 
 * @author randqm
 *
 */

public class MultipartBody extends ContentBody<List<AttributeKeyValuePair>> {

	/**
	 * The key-value pairs.
	 */
	private final List<AttributeKeyValuePair> kvps;
	
	/**
	 * The multipart boundary.
	 */
	private final String boundary;
	
	/**
	 * The total file size in the body.
	 */
	private int fileSize;

	
	/**
	 * Creates a new multipart body.
	 */
	public MultipartBody() {
		super(EncType.MULTIPART);
		
		this.kvps = new LinkedList<>();
		this.boundary = "----WebKitFormBoundary" + TextUtil.randomString(16, 16
				, TextSeed.DIGITS, TextSeed.LOWERCASE, TextSeed.UPPERCASE);
	}
	
	
	/**
	 * Adds a new key-value pair.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 * 
	 * @return The body.
	 */
	public MultipartBody add(String key, Object value) {
		return add(key, value, false);
	}
	
	/**
	 * Adds a new key-value pair.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 * 
	 * @param noReplace Whether to keep existing entries with the same key.
	 * 
	 * @return The body.
	 */
	public MultipartBody add(String key, Object value, boolean noReplace) {
		if (!noReplace) {
			AttributeKeyValuePair exists = null;
			
			for (AttributeKeyValuePair kvp : kvps) {
				if (kvp.getKey().equals(key)) {
					exists = kvp;
					break;
				}
			}
			if (Objects.nonNull(exists)) {
				kvps.remove(exists);
			}
		}
		kvps.add(new AttributeKeyValuePair(key, value));
		return this;
	}
	
	@Override
	protected void setContentHeaders(Headers headers) {
		super.setContentHeaders(headers);
		
		headers.add("Content-Type", getEncType().getContentTypeHeader() + "; boundary=" + boundary);
	}
	
	@Override
	protected void build(HTTPOutputStream httpStream, boolean prepare) throws IOException {
		fileSize = 0;
		boolean lastEntryWasFile = false;
		
		for (AttributeKeyValuePair kvp : kvps) {
			String key = new String(kvp.getKey().getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8);
			lastEntryWasFile = true;
			
			httpStream.writeLine("--" + boundary);
			
			if (Objects.isNull(kvp.getValue())) {
				writeOctetStreamPart(httpStream, key);
				continue;
			}
			if (kvp.getValue() instanceof byte[]) {
				writeOctetStreamPart(httpStream, prepare, key, (byte[])kvp.getValue());
				fileSize += ((byte[])kvp.getValue()).length;
				continue;
			}
			if (kvp.getValue() instanceof File) {
				if (isChunked()) {
					if (Objects.isNull(getChunkHandler())) {
						setChunkHandler(new ChunkHandler((File)kvp.getValue()));
					}
					getChunkHandler().prepare();
					fileSize += getChunkHandler().getChunkSize();
				} else {
					fileSize += (int)((File)kvp.getValue()).length();
				}
				writeFilePart(httpStream, prepare, key, (File)kvp.getValue());
				continue;
			}
			String value = kvp.getValue() instanceof String ? kvp.getValueAsString() : String.valueOf(kvp.getValue());
			value = new String(value.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8);
			lastEntryWasFile = false;
			
			writeStringPart(httpStream, key, value);
		}
		if (lastEntryWasFile) {
			httpStream.writeLine();
			httpStream.flush();
		}
		httpStream.writeLine("--" + boundary + "--");
	}
	
	@Override
	public MultipartBody setChunked(boolean chunked) {
		return (MultipartBody)super.setChunked(chunked);
	}
	
	/**
	 * Writes a string entry.
	 * 
	 * @param httpStream The HTTP output stream.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 * 
	 * @throws IOException 
	 */
	protected void writeStringPart(HTTPOutputStream httpStream, String key, String value) throws IOException {
		httpStream.writeLine("Content-Disposition: form-data; name=\"" + key + "\"");
		httpStream.writeLine("Content-Type: text/plain; charset=UTF-8");
		httpStream.writeLine();
		httpStream.writeLine(new String(value.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8));
		httpStream.flush();
	}
	
	/**
	 * Writes a file entry.
	 * 
	 * @param httpStream The HTTP output stream.
	 * 
	 * @param prepare Whether this is a preparation build or write.
	 * 
	 * @param key The key.
	 * 
	 * @param value The value.
	 * 
	 * @throws IOException 
	 */
	protected void writeFilePart(HTTPOutputStream httpStream, boolean prepare, String key, File value) throws IOException {
		httpStream.writeLine("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + value.getName() + "\"");
		httpStream.writeLine("Content-Type: " + MimeType.getByFile(value).getName());
		
		if (!isChunked()) {
			httpStream.writeLine("Content-Transfer-Encoding: binary");
		}
		httpStream.writeLine();
		httpStream.flush();
		
		if (!prepare) {
			writeFile(httpStream, value);
		}
		httpStream.writeLine();
		httpStream.flush();
	}
	
	/**
	 * Writes a file.
	 * 
	 * @param httpStream The HTTP output stream.
	 * 
	 * @param file The file to write.
	 * 
	 * @throws IOException
	 */
	protected void writeFile(HTTPOutputStream httpStream, File file) throws IOException {
		if (isChunked()) {
			getChunkHandler().writeChunk(httpStream);
			httpStream.flush();
			
			if (getChunkHandler().isFinished()) {
				getChunkHandler().close();
			}
			return;
		}
		try (FileInputStream fis = new FileInputStream(file)) {
			int bytesRead = -1;
			byte[] buffer = new byte[NetConfig.getConfig().getChunkSize()];
    	
			while ((bytesRead = fis.read(buffer)) != -1) {
				try {
					httpStream.write(buffer, 0, bytesRead);
				} catch (Exception ex) {
					httpStream.write(buffer, 0, bytesRead);
				}
			}
			httpStream.flush();
		}
	}
	
	/**
	 * Writes an octet-stream.
	 * 
	 * @param httpStream The HTTP output stream.
	 * 
	 * @param prepare Whether this is a preparation build or write.
	 * 
	 * @param key The key.
	 * 
	 * @param payload The payload to write.
	 * 
	 * @throws IOException
	 */
	protected void writeOctetStreamPart(HTTPOutputStream httpStream, boolean prepare, String key, byte[] payload) throws IOException {
		httpStream.writeLine("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"blob\"");
		httpStream.writeLine("Content-Type: application/octet-stream");
		httpStream.writeLine();
		
		if (!prepare) {
			httpStream.write(payload);
		}
		httpStream.writeLine();
		httpStream.flush();
	}
	
	/**
	 * Writes an empty octet-stream.
	 * 
	 * @param httpStream The HTTP output stream.
	 * 
	 * @param key The key.
	 * 
	 * @throws IOException
	 */
	protected void writeOctetStreamPart(HTTPOutputStream httpStream, String key) throws IOException {
		httpStream.writeLine("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"\"");
		httpStream.writeLine("Content-Type: application/octet-stream");
		httpStream.writeLine();
		httpStream.writeLine();
		httpStream.flush();
	}
	
	@Override
	protected void setContentLength(int contentLength) {
		super.setContentLength(contentLength + fileSize);
	}

	@Override
	public List<AttributeKeyValuePair> getContent() {
		return kvps;
	}

}
