package com.nattguld.http.response.bodies;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.exceptions.NetException;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.requests.Request;
import com.nattguld.http.response.ResponseInterpretor;
import com.nattguld.http.response.bodies.impl.FileResponseBody;
import com.nattguld.http.response.bodies.impl.StringResponseBody;
import com.nattguld.http.response.interpretors.FileInterpretor;
import com.nattguld.http.response.interpretors.StringInterpretor;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class ResponseBodyParser {

	
	/**
	 * Reads the request response body.
	 * 
	 * @param request The request.
	 * 
	 * @param responseHeaders The response headers.
	 * 
	 * @param in The input stream.
	 * 
	 * @return The response body.
	 * 
	 * @throws IOException
	 */
	public static IResponseBody<? extends Object> parseResponseBody(Request request, Headers responseHeaders, InputStream in) throws IOException {
		if (!request.isDecodeBody() && NetConfig.getConfig().isSaveDataMode()) {
			return new StringResponseBody("Body decoding is turned off for this request");
		}
		String contentEncoding = responseHeaders.getValueIgnoreCase("Content-Encoding"); //The content encoding of the response, will be null when it's plain text
		String contentLength = responseHeaders.getValueIgnoreCase("Content-Length");
		String contentType = responseHeaders.getValueIgnoreCase("Content-Type");
		String chunkedValue = responseHeaders.getValueIgnoreCase("Transfer-Encoding"); //The transfer encoding if any
		boolean chunked = Objects.nonNull(chunkedValue) && Objects.isNull(contentLength) && chunkedValue.equals("chunked"); //Whether the response is sent chunked or not

		int bodySize = 0;
		
		if (Objects.nonNull(contentLength)) {
			if (!Maths.isInteger(contentLength)) {
				throw new NetException("Content length for " + request.getUrl() + " is not a valid integer: " + contentLength);
			}
			bodySize = Maths.parseInt(contentLength, 0);
		}
		if (!chunked && Objects.isNull(contentLength)) {
			throw new NetException("Response for " + request.getUrl() + " is not chunked but has no Content-Length header");
		}
		boolean download = Objects.nonNull(request.getSavePath()) && Objects.nonNull(contentType);
		
		if (NetConfig.getConfig().isDebug()) {
			System.out.println("Parsing server response body for [" + request.getUrl() + "] with properties [Content-Encoding: " + contentEncoding 
					+ ", Transfer-Encoding: " + chunkedValue + ", Content-Length: " + bodySize + ", Content-Type: " + contentType + "]");
		}
		if (chunked) { //Read the chunks if the response is chunked
			if (Objects.nonNull(responseHeaders.getValueIgnoreCase("Location"))) { //When it's a redirect we don't need to read any body
				return new StringResponseBody("");
			}
			in = readChunks(in);
			
			if (Objects.isNull(in)) {
				System.err.println("Failed to read chunks on " + request.getUrl());
				
				if (NetConfig.getConfig().isDebug()) {
					throw new NetException("Failed to read chunks on " + request.getUrl());
				}
				return download ? new FileResponseBody(null) : new StringResponseBody("Failed to read chunks");
			}
			if (bodySize > 0 && bodySize != in.available()) {
				System.err.println("Unexpected body size for " + request.getUrl() + ", received " + in.available() + " instead of " + bodySize);
			}
			bodySize = in.available();
		}
		if (Objects.nonNull(contentLength) && contentLength.equals("0")) {
			if (NetConfig.getConfig().isDebug()) {
				System.out.println("Content length is zero for " + request.getUrl() + ", no body to parse");
			}
			return new StringResponseBody("");
		}
		ResponseInterpretor<? extends Object> interpretor = download 
				? new FileInterpretor(bodySize, request.getSavePath())
						: new StringInterpretor(bodySize, contentEncoding, contentType);
				
		ExecutorService executor = null;
				
		if (Objects.nonNull(request.getProgressListener())) {
			executor = Executors.newSingleThreadExecutor();
					
			executor.submit(new Runnable() {
				@Override
				public void run() {
					while (interpretor.getProgress() < 100) {
						request.getProgressListener().setProgress(interpretor.getProgress());
					}
				}
			});
		}
		IResponseBody<? extends Object> responseBody = interpretor.interpret(in);
				
		if (Objects.nonNull(executor)) {
			executor.shutdownNow();
		}
		return responseBody;
	}
	
	/**
	 * Writes the body chunks to an unchunked input stream.
	 * 
	 * @param in The original input stream.
	 * 
	 * @return The unchunked input stream.
	 * 
	 * @throws IOException
	 */
	private static InputStream readChunks(InputStream in) throws IOException {
		byte[] bodyPayload = null; //Will hold our unchunked body payload once received
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			int bytesRead = 0; //The bytes read in the read session
			byte[] lineBuffer = new byte[1024]; //The line reading buffer
			int chunkSize = 0; //The current chunk size we're supposed to receive
			
			while (true) {
				byte b = (byte)in.read();
				
				if (b < 0) { //We reached the end of the input stream
					break;
				}
				lineBuffer[bytesRead++] = b;
				
				if (b != '\n') { //Keep collecting bytes if there's no new line indicated
					continue;
				}
				String s = new String(lineBuffer, 0, bytesRead, "ASCII7").trim(); //Build a string out of our collected bytes and trim off the line break
				
				if (s.isEmpty()) { //Nothing to handle here
					continue;
				}
				if (s.equals("0")) { //End of body indicated by the server
					break;
				}
				try { //Check if the line is a hex giving us the chunk size and convert to decimal if so
					chunkSize = Maths.hexToInteger(s); //Integer.parseInt(s.trim(), 16);
						
				} catch (NumberFormatException ex) { //Get rid of the received bytes if it's not a chunk size indicator
					baos.write(lineBuffer, 0, bytesRead); //Write the line buffer to our unchunked body stream
					bytesRead = 0; //Reset the bytes read for next read
					lineBuffer = new byte[1024]; //Clear the buffer for next read
					continue;
				}
				bytesRead = 0; //Reset the bytes read for next read
				lineBuffer = new byte[1024]; //Clear the buffer for next read
				
				int received = 0; //Holds how many bytes we've received in total for this chunk
				
				while (received < chunkSize) { //As long as we didn't receive the expected bytes, keep waiting and reading
					byte[] chunkBuffer = new byte[chunkSize - received]; //Create a byte buffer with the chunk size
					int readCount = in.read(chunkBuffer); //Fill up the chunk buffer and retrieve the amount of bytes we've read
					baos.write(chunkBuffer, 0, readCount); //Write the chunk buffer to our unchunked body stream
					received += readCount; //Add the count we read to our total received count
				}
			}
			bodyPayload = baos.toByteArray(); //Write the received bytes to our body payload array
		}
		if (Objects.isNull(bodyPayload)) {
			System.err.println("No body payload received after reading chunks");
			return null;
		}
		if (bodyPayload.length <= 0) {
			System.err.println("Empty payload received after reading chunks");
			return null;
		}
		return new ByteArrayInputStream(bodyPayload);
	}
	
}
