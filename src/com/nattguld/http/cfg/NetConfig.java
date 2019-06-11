package com.nattguld.http.cfg;

import java.util.Objects;

import com.nattguld.http.proxies.cfg.ProxyConfigs;

/**
 * 
 * @author randqm
 *
 */

public class NetConfig {
	
	/**
	 * Whether developer mode is enabled or not.
	 */
	public static boolean DEVELOPER_MODE = false;
	
	/**
	 * The global instance of the net configurations.
	 */
	private static NetConfig globalInstance;
	
	/**
	 * Whether to debug network operations or not.
	 */
	private boolean debug = false;
	
	/**
	 * Whether to send do not track requests or not.
	 */
	private boolean doNotTrack = false;
	
	/**
	 * Whether data transfer progress should be tracker or not.
	 * Doing so will result in slightly slower transfer times.
	 */
	private boolean transferProgress = true;
	
	/**
	 * The default connection timeout.
	 */
	private int connectionTimeout = 120;
	
	/**
	 * The default read timeout.
	 */
	private int readTimeout = 120;
	
	/**
	 * The preferred chunk size for chunked uploads.
	 */
	private int chunkSize = 8388608;
	
	/**
	 * The proxy configurations.
	 */
	private ProxyConfigs proxyConfigs = new ProxyConfigs();
	
	
	/**
	 * Modifies the debug state.
	 * 
	 * @param debug The new debug state.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Retrieves whether to debug or not.
	 * 
	 * @return The result.
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Modifies the do not track state.
	 * 
	 * @param doNotTrack The new do not track state.
	 */
	public void setDoNotTrack(boolean doNotTrack) {
		this.doNotTrack = doNotTrack;
	}
	
	/**
	 * Retrieves the do not track state.
	 * 
	 * @return The result.
	 */
	public boolean isDoNotTrack() {
		return doNotTrack;
	}
	
	/**
	 * Modifies the connection timeout.
	 * 
	 * @param connectionTimeout The new connection timeout.
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	/**
	 * Retrieves the connection timeout.
	 * 
	 * @return The connection timeout.
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	
	/**
	 * Modifies the read timeout.
	 * 
	 * @param readTimeout The new read timeout.
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	/**
	 * Retrieves the read timeout.
	 * 
	 * @return The read timeout.
	 */
	public int getReadTimeout() {
		return readTimeout;
	}
	
	/**
	 * Modifies the chunk size.
	 * 
	 * @param chunkSize The chunk size.
	 */
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
	/**
	 * Retrieves the chunk size.
	 * 
	 * @return The chunk size.
	 */
	public int getChunkSize() {
		return chunkSize;
	}
	
	/**
	 * Modifies the transfer progress state.
	 * 
	 * @param transferProgress The new transfer progress state.
	 */
	public void setTransferProgress(boolean transferProgress) {
		this.transferProgress = transferProgress;
	}
	
	/**
	 * Retrieves whether to track the transfer progress or not.
	 * 
	 * @return The result.
	 */
	public boolean isTransferProgress() {
		return transferProgress;
	}
	
	/**
	 * Modifies the proxy configurations.
	 * 
	 * @param proxyConfigs The new proxy configurations.
	 */
	public void setProxyConfigs(ProxyConfigs proxyConfigs) {
		this.proxyConfigs = proxyConfigs;
	}
	
	/**
	 * Retrieves the proxy configurations.
	 * 
	 * @return The proxy configurations.
	 */
	public ProxyConfigs getProxyConfigs() {
		return proxyConfigs;
	}
	
	/**
	 * Retrieves the global instance.
	 * 
	 * @return The global instance.
	 */
	public static NetConfig getGlobalInstance() {
		return getGlobalInstance(Objects.isNull(globalInstance) ? new NetConfig() : null);
	}
	
	/**
	 * Retrieves the global instance.
	 * 
	 * @param The global instance to set.
	 * 
	 * @return The global instance.
	 */
	public static NetConfig getGlobalInstance(NetConfig newInstance) {
		if (Objects.nonNull(newInstance)) {
			globalInstance = newInstance;
		}
		return globalInstance;
	}

}
