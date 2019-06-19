package com.nattguld.http.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.Objects;

import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class NetUtil {
	
	/**
	 * The default user agent.
	 */
	public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
	
	/**
	 * The default browser language.
	 */
	public static final String DEFAULT_BROWSER_LANGUAGE = "en-US,en;q=0.9";
	
	/**
	 * The desktop CPU core options.
	 */
	private static final int[] DESKTOP_CORES = new int[] {
			1, 2, 2, 2, 4, 4, 4
	};
	
	/**
	 * The mobile CPU core options.
	 */
	private static final int[] MOBILE_CORES = new int[] {
			2, 2, 2, 4, 4, 4, 8, 8, 8
	};
	
	/**
	 * The desktop screen resolution options.
	 */
	private static final int[][] DESKTOP_SCREEN_RESOLUTIONS = new int[][] {
		{1366, 768},{1366, 768},{1366, 768},
		{1920, 1080},{1920, 1080},{1920, 1080},
		{1440, 900},
		{1600, 900},
		{1536, 864},
		{1280, 800},
	};
	
	/**
	 * The mobile screen resolution options.
	 */
	private static final int[][] MOBILE_SCREEN_RESOLUTIONS = new int[][] {
		{360, 640},{360, 640},{360, 640},{360, 640},
		{375, 667},{375, 667},
		{414, 736},
		{720, 1280},
		{320, 568},
	};
	
	/**
	 * The desktop CPU cores.
	 * 
	 * @return The desktop CPU cores.
	 */
	public static int getDesktopCores() {
		return DESKTOP_CORES[Maths.random(DESKTOP_CORES.length)];
	}
	
	/**
	 * The mobile CPU cores.
	 * 
	 * @return The mobile CPU cores.
	 */
	public static int getMobileCores() {
		return MOBILE_CORES[Maths.random(MOBILE_CORES.length)];
	}
	
	/**
	 * Retrieves a desktop screen resolution.
	 * 
	 * @return The screen resolution.
	 */
	public static int[] getDesktopScreenResolution() {
		return DESKTOP_SCREEN_RESOLUTIONS[Maths.random(DESKTOP_SCREEN_RESOLUTIONS.length)];
	}
	
	/**
	 * Retrieves a mobile screen resolution.
	 * 
	 * @return The screen resolutions.
	 */
	public static int[] getMobileScreenResolution() {
		return MOBILE_SCREEN_RESOLUTIONS[Maths.random(MOBILE_SCREEN_RESOLUTIONS.length)];
	}
    
    /**
     * Retrieves the MAC address of the device.
     * 
     * @return The MAC address.
     */
    public static String getMacAddress() {
    	try {
    		Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
	    
    		while (networks.hasMoreElements()) {
    			NetworkInterface network = networks.nextElement();
    			byte[] mac = network.getHardwareAddress();
		
    			if (Objects.nonNull(mac)) {
    				StringBuilder sb = new StringBuilder();
		    
    				for (int i = 0; i < mac.length; i ++) {
    					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
    				}
    				return sb.toString();
    			}
    		}
    	} catch (SocketException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
	/**
	 * Retrieves the base url of the request.
	 * 
	 * @param url The url.
	 * 
	 * @return The base url.
	 */
	public static String getBaseUrl(String url) {
		return (url.startsWith("https") ? "https://" : "http://") + getDomain(url);
	}
	
	/**
	 * Retrieves the plain domain from a given url.
	 * 
	 * @param url The url.
	 * 
	 * @return The domain.
	 */
	public static String getDomain(String url) {
		String[] args = url.split("/");
		return url.startsWith("http") ? args[2] : args[0];
	}
	
	/**
	 * Encodes a url.
	 * 
	 * @param url The url.
	 * 
	 * @return The encoded url.
	 */
	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return url;
		}
	}
	
	/**
	 * Attempts to download a file.
	 * 
	 * @param downloadUrl The download url.
	 * 
	 * @param savePath The save path.
	 * 
	 * @throws IOException 
	 * 
	 * @throws MalformedURLException 
	 */
	public static void downloadFile(String downloadUrl, String savePath) throws MalformedURLException, IOException {
		ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(downloadUrl).openStream());
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
			try (FileChannel fileChannel = fileOutputStream.getChannel()) {
				fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			}
		}
	}

}
