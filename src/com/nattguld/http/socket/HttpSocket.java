package com.nattguld.http.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
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
import com.nattguld.http.proxies.ProxyManager;
import com.nattguld.http.response.decode.impl.HeaderDecoder;
import com.nattguld.http.ssl.SSLManager;

/**
 * 
 * @author randqm
 *
 */

public class HttpSocket {
	
	/**
	 * Holds the SSL hosts.
	 */
	public static final List<String> SSL_HOSTS = new ArrayList<>();
	
	/**
	 * Holds the host resolves.
	 */
	private final List<String> hostResolves = new ArrayList<>();

	
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
		//socket.setSoLinger(true, 0); //TODO Experimental
		socket.setReuseAddress(true); //TODO Experimental - Standard: False
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
	public ITestSocket connect(HttpProxy httpProxy, String host, int port, Browser browser, boolean ssl) throws Exception {
		if (httpProxy == ProxyManager.INVALID_PROXY) {
			throw new IOException("Invalid Proxy");
		}
		if (hostResolves.contains(host)) {
			host = InetAddress.getByName(host).getHostAddress();
		}
		return ssl ? connectSSL(httpProxy, host, port, browser) 
						: connect(httpProxy, host, port, browser);
	}
	
	/**
	 * Connects to a standard socket.
	 * 
	 * @param httpProxy The http proxy.
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
	private ITestSocket connect(HttpProxy httpProxy, String host, int port, Browser browser) throws IOException {
		Socket socket = Objects.isNull(httpProxy) ? new Socket() : new Socket(httpProxy.getHost(), httpProxy.getPort());
		setSocketConfigs(socket, browser);

		try {
			if (Objects.nonNull(httpProxy) && httpProxy.hasAuthentication()) {
				doTunnelHandshake(socket, httpProxy, host, port, browser);
			}
			if (!socket.isConnected()) {
				socket.connect(new InetSocketAddress(host, port));
			}
			return new StandardSocket(socket);
			
		} catch (IOException ex) {
			if (hostResolves.contains(host)) {
				hostResolves.remove(host);
				ex.printStackTrace();
				return new StandardSocket(socket);
			}
			hostResolves.add(host);
			return connect(httpProxy, host, port, browser);
		}
	}
	
	/**
	 * Retrieves an SSL socket.
	 * 
	 * @param httpProxy The http proxy.
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
	private ITestSocket connectSSL(HttpProxy httpProxy, String host, int port, Browser browser) throws IOException {
		SSLSocketFactory sslSocketFactory = SSLManager.buildSocketFactory();
		ITestSocket testSocket = null;
		SSLSocket sslSocket = null;
		
		if (Objects.nonNull(httpProxy)) {
			Socket tunnel = new Socket(httpProxy.getHost(), httpProxy.getPort());
			
			setSocketConfigs(tunnel, browser);
				
			doTunnelHandshake(tunnel, httpProxy, host, 443, browser);
				
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(tunnel, host, port, true);
			
			testSocket = new TunnelSocket(sslSocket, tunnel);
			
		} else {
			sslSocket = (SSLSocket)sslSocketFactory.createSocket(host, 443);
			testSocket = new StandardSocket(sslSocket);
		}
		setSocketConfigs(sslSocket, browser);
			
		try {
			sslSocket.startHandshake();
				
			if (!SSL_HOSTS.contains(host)) {
				SSL_HOSTS.add(host);
			}
			return testSocket;
				
		} catch (IOException ex) {
			if (hostResolves.contains(host)) {
				hostResolves.remove(host);
				ex.printStackTrace();
				return testSocket;
			}
			hostResolves.add(host);
			return connectSSL(httpProxy, host, port, browser);
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
        	System.out.println("Tunneled through proxy for " + host + ":" + port);
        }
    }

}
