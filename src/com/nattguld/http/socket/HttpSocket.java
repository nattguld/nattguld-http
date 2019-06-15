package com.nattguld.http.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.response.decode.impl.HeaderDecoder;
import com.nattguld.http.ssl.SSLManager;
import com.nattguld.util.hashing.Hasher;

/**
 * 
 * @author randqm
 *
 */

public class HttpSocket {
	
	/**
	 * The maximum amount of resolve hostname entries to keep.
	 */
	private static final int MAX_RESOLVE_HOSTNAME_SIZE = 1000;
	
	/**
	 * Holds the hosts that need their host name resolved.
	 */
	private static List<String> resolveHostname = new CopyOnWriteArrayList<>();
	
	
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
	public static Socket connect(HttpProxy proxy, String host, Browser browser, boolean ssl, int port) throws Exception {
		Socket socket = (ssl || (Objects.nonNull(proxy) && proxy.hasAuthentication()))
				? connectSSL(proxy, host, browser, port) 
						: connect(proxy, host, browser, port);
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
	private static Socket connect(HttpProxy proxy, String host, Browser browser, int port) throws IOException {
		System.out.println("Regular [" + host + "][" + port + "][" + proxy + "]");
		
		//InetAddress ia = InetAddress.getByName(host);
		//host = ia.getHostAddress();
		
		Socket socket = new Socket(Objects.nonNull(proxy) ? proxy.toJavaProxy() : Proxy.NO_PROXY);
		socket.connect(new InetSocketAddress(host, port));
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
	private static Socket connectSSL(HttpProxy proxy, String host, Browser browser, int port) throws IOException {
		System.out.println("connect SSL [" + host + "][" + port + "][" + proxy + "]");
		String contactHost = host;
		
		if (resolveHostname.contains(host)) {
			InetAddress ia = InetAddress.getByName(contactHost);
			contactHost = ia.getHostAddress();
		}
		SSLSocketFactory sslSocketFactory = SSLManager.buildSocketFactory();
		SSLSocket sslSocket = null;
		
		if (Objects.nonNull(proxy) && !proxy.hasAuthentication()) {
			System.out.println("SSL proxy with no auth");
			
			Socket tunnel = new Socket(proxy.getHost(), proxy.getPort());
			
			doTunnelHandshake(tunnel, proxy, contactHost, browser, port == 80 ? 443 : port);
			
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(tunnel, contactHost, port, true);
			
		} else {
			System.out.println("SSL no proxy or proxy with auth");
			
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(contactHost, port == 80 ? 443 : port);
		}
		try {
			sslSocket.startHandshake();
			
		} catch (Exception ex) {
			if (resolveHostname.contains(host)) {
				ex.printStackTrace();
				return null;
			}
			resolveHostname.add(host);
			
			if (resolveHostname.size() > MAX_RESOLVE_HOSTNAME_SIZE) {
				resolveHostname.remove(0);
			}
			return connectSSL(proxy, host, browser, port);
		}
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
    private static void doTunnelHandshake(Socket tunnel, HttpProxy proxy, String host, Browser browser, int port) throws IOException {
        OutputStream out = tunnel.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8), true);
        
        writer.println("CONNECT " + host + ":" + port + " " + browser.getHttpVersion().getName());
        writer.println("Host: " + host + ":" + port);
        writer.println("User-Agent: " + browser.getUserAgent());
        writer.println("Connection: keep-alive");
        
        if (Objects.nonNull(proxy) && proxy.hasAuthentication()) {
        	String encoded = Hasher.base64(proxy.getUsername() + ":" + proxy.getPassword());
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
        if (NetConfig.getConfig().isDebug()) {
        	System.out.println("Tunneled through proxy for " + host + ":" + 443);
        }
    }

}
