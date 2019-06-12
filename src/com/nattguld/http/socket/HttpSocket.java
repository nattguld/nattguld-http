package com.nattguld.http.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.response.decode.impl.HeaderDecoder;
import com.nattguld.http.ssl.SSLManager;

/**
 * 
 * @author randqm
 *
 */

public class HttpSocket {
	
	
	/**
	 * Attempts to connect to the target server.
	 * 
	 * @param proxy The proxy to tunnel through if any.
	 * 
	 * @param url The url to connect to.
	 * 
	 * @param ssl Whether to use SSL or not.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @throws Exception
	 */
	public static Socket connect(HttpProxy proxy, String host, Browser browser, boolean ssl, int forcePort) throws Exception {
		Socket socket = (ssl || (Objects.nonNull(proxy) && proxy.hasAuthentication()))
				? connectSSL(proxy, host, browser, forcePort) 
						: connect(proxy, host, browser, forcePort);
		socket.setSoTimeout(browser.getConnectionTimeout() * 1000);
		socket.setKeepAlive(true);
		socket.setReuseAddress(true);
		socket.setSendBufferSize(65536);
		socket.setReceiveBufferSize(65536);
		socket.setTcpNoDelay(true);
		return socket;
	}
	
	/**
	 * Retrieves a default socket.
	 * 
	 * @param proxy The proxy to tunnel through if any.
	 * 
	 * @param host The target server host.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @return The socket.
	 * 
	 * @throws IOException
	 */
	private static Socket connect(HttpProxy proxy, String host, Browser browser, int forcePort) throws IOException {
		Socket socket = new Socket(Objects.nonNull(proxy) ? proxy.toJavaProxy() : Proxy.NO_PROXY);
		socket.connect(new InetSocketAddress(host, forcePort == -1 ? 80 : forcePort));
		return socket;
	}
	
	/**
	 * Retrieves an SSL socket.
	 * 
	 * @param proxy The proxy to tunnel through if any.
	 * 
	 * @param host The target server host.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @return The SSL socket.
	 * 
	 * @throws IOException
	 */
	private static Socket connectSSL(HttpProxy proxy, String host, Browser browser, int forcePort) throws IOException {
		//InetAddress ia = InetAddress.getByName(host);
		//host = ia.getHostAddress();
		
		SSLSocketFactory sslSocketFactory = SSLManager.buildSocketFactory();
		SSLSocket sslSocket = null;
		
		if (Objects.nonNull(proxy)) {
			Socket tunnel = new Socket(proxy.getHost(), proxy.getPort());
			doTunnelHandshake(tunnel, proxy, host, browser, forcePort);
			
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(tunnel, host, forcePort == -1 ? 80 : forcePort, true);
			sslSocket.startHandshake();
			
		} else {
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(host, forcePort == -1 ? 443 : forcePort);
		}
		//sslSocket.startHandshake();
		return sslSocket;
	}
	
	/**
	 * Connect with our tunnel socket to a given host.
	 * 
	 * @param tunnel The tunnel socket.
	 * 
	 * @param proxy The proxy if any.
	 * 
	 * @param host The host to connect to.
	 * 
	 * @param port The port.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @throws IOException
	 */
    private static void doTunnelHandshake(Socket tunnel, HttpProxy proxy, String host, Browser browser, int forcePort) throws IOException {
        OutputStream out = tunnel.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);
        
        writer.println("CONNECT " + host + ":" + (forcePort == -1 ? 443 : forcePort) + " " + browser.getHttpVersion().getName());
        writer.println("Host: " + host + ":" + (forcePort == -1 ? 443 : forcePort));
        writer.println("User-Agent: " + browser.getUserAgent());
        writer.println("Connection: keep-alive");
        
        if (Objects.nonNull(proxy) && proxy.hasAuthentication()) {
        	String encoded = new String(java.util.Base64.getEncoder().encode((proxy.getUsername() + ":" + proxy.getPassword()).getBytes())).replace("\r\n", "");
        	writer.println("Proxy-Authorization: Basic " + encoded);
        }
        writer.println();
        writer.flush();
        out.flush();
        
        InputStream in = tunnel.getInputStream();
        
        HeaderDecoder hd = new HeaderDecoder();
        hd.decode(in);
        
        if (hd.getResponseStatus().getCode() != 200) {
        	throw new IOException("Unable to tunnel through proxy (" + hd.getResponseStatus() + ")");
        }
        if (NetConfig.getGlobalInstance().isDebug()) {
        	System.out.println("Tunneled through proxy for " + host + ":" + 443);
        }
    }

}