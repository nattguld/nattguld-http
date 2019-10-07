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
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.nattguld.http.HTTPCode;
import com.nattguld.http.browser.Browser;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.headers.Headers;
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
	 * The maximum amount of backlog entries to keep.
	 */
	private static final int MAX_BACKLOG_SIZE = 5000;
	
	/**
	 * Holds the hosts that need their host name resolved.
	 */
	private static List<String> resolveHostName = new CopyOnWriteArrayList<>();
	
	/**
	 * Holds the SSL hosts.
	 */
	private static List<String> sslHosts = new CopyOnWriteArrayList<>();
	
	
	/**
	 * Adds a given value to a given list's backlog.
	 * 
	 * @param list The list.
	 * 
	 * @param value The value.
	 */
	private static void backlog(List<String> list, String value) {
		if (list.contains(value)) {
			list.remove(value);
		}
		list.add(value);
		
		if (list.size() > MAX_BACKLOG_SIZE) {
			list.remove(0);
		}
	}
	
	/**
	 * Logs an SSL host.
	 * 
	 * @param host The host.
	 */
	private static void logSSL(String host) {
		backlog(sslHosts, host);
	}
	
	/**
	 * Logs a resolve host name.
	 * 
	 * @param hostName The host name.
	 */
	private static void logResolveHostName(String hostName) {
		backlog(resolveHostName, hostName);
	}
	
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
	public Socket connect(HttpProxy proxy, String host, Browser browser, boolean ssl, int port) throws Exception {
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
	private Socket connect(HttpProxy proxy, String host, Browser browser, int port) throws IOException {
		if (sslHosts.contains(host)) {
			return connectSSL(proxy, host, browser, port);
		}
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
	private Socket connectSSL(HttpProxy proxy, String host, Browser browser, int port) throws IOException {
		logSSL(host);
		
		String contactHost = host;
		
		if (resolveHostName.contains(host)) {
			InetAddress ia = InetAddress.getByName(contactHost);
			contactHost = ia.getHostAddress();
		}
		SSLSocketFactory sslSocketFactory = SSLManager.buildSocketFactory();
		SSLSocket sslSocket = null;
		
		if (Objects.nonNull(proxy)) {
			Socket tunnel = new Socket(proxy.getHost(), proxy.getPort());
			
			doTunnelHandshake(tunnel, proxy, contactHost, browser, 443);
			
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(tunnel, contactHost, port, true);
			
		} else {
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(contactHost, 443);
		}
		try {
			sslSocket.startHandshake();
			
		} catch (Exception ex) {
			if (resolveHostName.contains(host)) {
				ex.printStackTrace();
				return sslSocket;
			}
			logResolveHostName(host);
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
    private void doTunnelHandshake(Socket tunnel, HttpProxy proxy, String host, Browser browser, int port) throws IOException {
    	Headers headers = new Headers();
    	headers.add("Host", host + ":" + port);
    	headers.add("User-Agent", browser.getUserAgent());
    	headers.add("Connection", "keep-alive");
    	
    	if (Objects.nonNull(proxy) && proxy.hasAuthentication()) {
        	headers.add("Proxy-Connection", "keep-alive");
        	headers.add("Proxy-Authorization", "Basic " + proxy.getBase64Auth());
        }
    	StringBuilder raw = new StringBuilder();
    	
        OutputStream out = tunnel.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, Charset.forName("UTF-8").newEncoder()), true) {
			@Override
			public void println(String s) {
				super.println(s);
				
				if (NetConfig.getConfig().isDebug()) {
					raw.append(s);
					raw.append(System.lineSeparator());
				}
			}
		};
        writer.println("CONNECT " + host + ":" + port + " " + browser.getHttpVersion().getName());
        
        for (Entry<String, String> header : headers.getHeaders().entrySet()) {
			writer.println(header.getKey() + ": " + header.getValue());
		}
        if (NetConfig.getConfig().isDebug()) {
			System.err.println(raw.toString());
		}
        writer.println();
        writer.flush();
        out.flush();
        
        InputStream in = tunnel.getInputStream();
        
        HeaderDecoder hd = new HeaderDecoder();
        hd.decode(in);
        
        if (hd.getResponseStatus().getHttpCode() != HTTPCode.OK) {
        	in.close();
        	throw new IOException("Unable to tunnel through proxy (" + hd.getResponseStatus() + ")");
        }
        if (NetConfig.getConfig().isDebug()) {
        	System.out.println("Tunneled through proxy for " + host + ":" + 443);
        }
    }

}
