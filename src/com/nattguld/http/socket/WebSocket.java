package com.nattguld.http.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.google.gson.JsonObject;
import com.nattguld.http.HttpClient;
import com.nattguld.http.browser.Browser;
import com.nattguld.http.content.EncType;
import com.nattguld.http.content.cookies.Cookie;
import com.nattguld.http.headers.Headers;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.ProxyManager;
import com.nattguld.http.requests.impl.GetRequest;
import com.nattguld.http.response.RequestResponse;
import com.nattguld.http.response.decode.impl.HeaderDecoder;

/**
 * 
 * @author randqm
 *
 */

public class WebSocket {

	
	
    private static void doTunnelHandshake(Socket tunnel, HttpProxy proxy, String host, int port, Browser browser) throws IOException {
		OutputStream out = tunnel.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);
	        
		writer.println("GET ws://recommend.chaturbate.com:8443/ws HTTP/1.1");
		writer.println("Host: server.example.com");
		writer.println("Upgrade: websocket");
		writer.println("Connection: Upgrade");
		writer.println("Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==");
		writer.println("Origin: http://example.com");
		writer.println("Sec-WebSocket-Protocol: chat, superchat");
		writer.println("Sec-WebSocket-Version: 13");
	        
	        if (Objects.nonNull(proxy) && proxy.hasAuthentication()) {
	        	//String encoded = new sun.misc.BASE64Encoder().encodeBuffer((proxy.getUsername() + ":" + proxy.getPassword()).getBytes()).replace("\r\n", "");
	        	//writer.println("Proxy-Authorization: Basic " + encoded);
	        }
	        writer.println();
	        writer.flush();
	        out.flush();
	        
	        InputStream in = tunnel.getInputStream();
	        
	        HeaderDecoder hd = new HeaderDecoder();
	        hd.decode(in);
	        
	      /*  if (hd.getResponseStatus().getCode() != 200) {
	        	throw new IOException("Unable to tunnel through proxy (" + hd.getResponseStatus() + ")");
	        }*/
	        /*
	       //RESPONSE
	        HTTP/1.1 101 Switching Protocols
	        Upgrade: websocket
	        Connection: Upgrade
	        Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
	        Sec-WebSocket-Protocol: chat*/
	}

}
