package com.nattguld.http.cfg;

import com.nattguld.data.cfg.Config;
import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.http.DataCounter;

/**
 * 
 * @author randqm
 *
 */

public class NetConfig extends Config {
	
	/**
	 * Whether developer mode is enabled or not.
	 */
	public boolean developerMode = false;
	
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
	private int chunkSize = 11534335;
	
	/**
	 * The data counter for 4G connections.
	 */
	private DataCounter cellularDataCounter;
	
	/**
	 * Whether save data mode is used or not.
	 */
	private boolean saveDataMode;
	

	@Override
	protected void read(JsonReader reader) {
		this.developerMode = reader.getAsBoolean("developer_mode", false);
		this.debug = reader.getAsBoolean("debug", false);
		this.doNotTrack = reader.getAsBoolean("do_not_track", false);
		this.transferProgress = reader.getAsBoolean("transfer_progress", true);
		this.connectionTimeout = reader.getAsInt("connection_timeout", 90);
		this.readTimeout = reader.getAsInt("read_timeout", 120);
		this.chunkSize = reader.getAsInt("chunk_size", 11534336);
		this.cellularDataCounter = (DataCounter)reader.getAsObject("data_counter_cellular", DataCounter.class, new DataCounter());
		this.saveDataMode = reader.getAsBoolean("save_data_mode", false);
	}

	@Override
	protected void write(JsonWriter writer) {
		writer.write("developer_mode", developerMode);
		writer.write("debug", debug);
		writer.write("do_not_track", doNotTrack);
		writer.write("transfer_progress", transferProgress);
		writer.write("connection_timeout", connectionTimeout);
		writer.write("read_timeout", readTimeout);
		writer.write("chunk_size", chunkSize);
		writer.write("data_counter_cellular", cellularDataCounter);
		writer.write("save_data_mode", saveDataMode);
	}
	
	@Override
	protected String getSaveFileName() {
		return ".http_config";
	}
	
	/**
	 * Retrieves the cellular data counter.
	 * 
	 * @return The data counter.
	 */
	public DataCounter getCellularDataCounter() {
		return cellularDataCounter;
	}
	
	/**
	 * Modifies whether to use save data mode or not.
	 * 
	 * @param saveDataMode The new state.
	 * 
	 * @return The config.
	 */
	public NetConfig setSaveDataMode(boolean saveDataMode) {
		this.saveDataMode = saveDataMode;
		return this;
	}
	
	/**
	 * Retrieves whether to use save data mode or not.
	 * 
	 * @return The result.
	 */
	public boolean isSaveDataMode() {
		return saveDataMode;
	}
	
	/**
	 * Modifies whether to use developer mode or not.
	 * 
	 * @param developerMode The new state.
	 * 
	 * @return The config.
	 */
	public NetConfig setDeveloperMode(boolean developerMode) {
		this.developerMode = developerMode;
		return this;
	}
	
	/**
	 * Retrieves whether to use developer mode or not.
	 * 
	 * @return The result.
	 */
	public boolean isDeveloperMode() {
		return developerMode;
	}
	
	/**
	 * Modifies the debug state.
	 * 
	 * @param debug The new debug state.
	 * 
	 * @return The config.
	 */
	public NetConfig setDebug(boolean debug) {
		this.debug = debug;
		return this;
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
	 * 
	 * @return The config.
	 */
	public NetConfig setDoNotTrack(boolean doNotTrack) {
		this.doNotTrack = doNotTrack;
		return this;
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
	 * 
	 * @return The config.
	 */
	public NetConfig setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
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
	 * 
	 * @return The config.
	 */
	public NetConfig setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
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
	 * 
	 * @return The config.
	 */
	public NetConfig setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
		return this;
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
	 * 
	 * @return The config.
	 */
	public NetConfig setTransferProgress(boolean transferProgress) {
		this.transferProgress = transferProgress;
		return this;
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
	 * Retrieves the config.
	 * 
	 * @return The config.
	 */
	public static NetConfig getConfig() {
		return (NetConfig)ConfigManager.getConfig(new NetConfig());
	}

}
