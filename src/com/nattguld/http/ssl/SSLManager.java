package com.nattguld.http.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author randqm
 *
 */

public class SSLManager {

	
	/**
	 * Builds & retrieves an SSL socket factory.
	 * 
	 * @return The SSL socket factory.
	 */
	public static SSLSocketFactory buildSocketFactory() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
						
					@Override
					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}
						
					@Override
					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}
				}
			};
			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
	    
			return sc.getSocketFactory();
			
		} catch (NoSuchAlgorithmException | KeyManagementException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
