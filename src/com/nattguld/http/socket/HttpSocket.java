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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

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
	 * Holds the host resolves.
	 */
	private final List<String> hostResolves = new ArrayList<>();
	
	/**
	 * Holds the SSL hosts.
	 */
	private final List<String> sslHosts = new ArrayList<>();

	
	/**
	 * Sets the default socket configs on a given socket.
	 * 
	 * @param socket The socket.
	 * 
	 * @param browser The virtual browser using the socket.
	 * 
	 * @throws SocketException 
	 */
	private void setSocketConfigs(Socket socket, Browser browser) throws SocketException {
		socket.setSoTimeout(browser.getConnectionTimeout() * 1000);
		socket.setKeepAlive(false); //TODO Experimental - usually: true
		socket.setSendBufferSize(65536);
		socket.setReceiveBufferSize(65536);
		socket.setTcpNoDelay(true);
		socket.setSoLinger(true, 0); //TODO Experimental
		socket.setReuseAddress(false); //TODO Experimental
	}
	
	/**
	 * Attempts to connect to the target server.
	 * 
	 * @param proxy The proxy to tunnel through if any.
	 * 
	 * @param host The target server host.
	 * 
	 * @param port The target server port.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @param ssl Whether to force the use of SSL or not.
	 * 
	 * @throws Exception
	 */
	public Socket connect(HttpProxy proxy, String host, int port, Browser browser, boolean ssl) throws Exception {
		return (ssl || (Objects.nonNull(proxy) && proxy.hasAuthentication()))
				? connectSSL(proxy, host, port, browser) 
						: connect(proxy, host, port, browser);
	}
	
	/**
	 * Connects to a standard socket.
	 * 
	 * @param proxy The proxy to tunnel through if any.
	 * 
	 * @param host The target server host.
	 * 
	 * @param port The target server port.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @return The socket.
	 * 
	 * @throws IOException
	 */
	private Socket connect(HttpProxy proxy, String host, int port, Browser browser) throws IOException {
		if (sslHosts.contains(host)) {
			return connectSSL(proxy, host, port, browser);
		}
		boolean isResolvable = hostResolves.contains(host);
		String contactHost = host;
		
		if (isResolvable) {
			try {
				contactHost = InetAddress.getByName(host).getHostAddress();
				
			} catch (UnknownHostException ex) {
				throw ex;
			}
		}
		Socket socket = new Socket(Objects.nonNull(proxy) ? proxy.toJavaProxy() : Proxy.NO_PROXY);
		setSocketConfigs(socket, browser);
		
		try {
			socket.connect(new InetSocketAddress(contactHost, port));
			return socket;
			
		} catch (IOException ex) {
			if (isResolvable) {
				hostResolves.remove(host);
				ex.printStackTrace();
				return socket;
			}
			hostResolves.add(host);
			return connect(proxy, host, port, browser);
		}
	}
	
	/**
	 * Retrieves an SSL socket.
	 * 
	 * @param proxy The proxy to tunnel through if any.
	 * 
	 * @param host The target server host.
	 * 
	 * @param port The target server port.
	 * 
	 * @param browser The browser configurations.
	 * 
	 * @return The SSL socket.
	 * 
	 * @throws IOException
	 */
	private Socket connectSSL(HttpProxy proxy, String host, int port, Browser browser) throws IOException {
		boolean isResolvable = hostResolves.contains(host);
		String contactHost = host;
		
		if (isResolvable) {
			try {
				contactHost = InetAddress.getByName(host).getHostAddress();
				
			} catch (UnknownHostException ex) {
				throw ex;
			}
		}
		SSLSocketFactory sslSocketFactory = SSLManager.buildSocketFactory();
		SSLSocket sslSocket = null;
		
		if (Objects.nonNull(proxy)) {
			Socket tunnel = new Socket(proxy.getHost(), proxy.getPort());
			setSocketConfigs(tunnel, browser);
				
			doTunnelHandshake(tunnel, proxy, contactHost, 443, browser);
				
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(tunnel, contactHost, port, true);
			
		} else {
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(contactHost, 443);
		}
		setSocketConfigs(sslSocket, browser);
			
		try {
			sslSocket.startHandshake();
				
			if (!sslHosts.contains(host)) {
				sslHosts.add(host);
			}
			return sslSocket;
				
		} catch (IOException ex) {
			if (isResolvable) {
				hostResolves.remove(host);
				ex.printStackTrace();
				return sslSocket;
			}
			hostResolves.add(host);
			return connectSSL(proxy, host, port, browser);
		}
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
    private void doTunnelHandshake(Socket tunnel, HttpProxy proxy, String host, int port, Browser browser) throws IOException {
    	Headers headers = new Headers();
    	headers.add("Host", host + ":" + port);
    	headers.add("User-Agent", browser.getUserAgent());
    	headers.add("Connection", "keep-alive"); //close
    	
    	if (Objects.nonNull(proxy) && proxy.hasAuthentication()) {
        	headers.add("Proxy-Connection", "keep-alive");
        	headers.add("Proxy-Authorization", "Basic " + proxy.getAuthCreds().getBase64Auth());
        }
    	StringBuilder raw = new StringBuilder();
    	
        OutputStream out = tunnel.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(tunnel.getOutputStream(), Charset.forName("UTF-8").newEncoder()), true) {
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
        	writer.close();
        	out.close();
        	in.close();
        	throw new IOException("Unable to tunnel through proxy (" + hd.getResponseStatus() + ")");
        }
        if (NetConfig.getConfig().isDebug()) {
        	System.out.println("Tunneled through proxy for " + host + ":" + 443);
        }
    }

}
