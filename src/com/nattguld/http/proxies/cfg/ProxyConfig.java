package com.nattguld.http.proxies.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.reflect.TypeToken;
import com.nattguld.data.cfg.Config;
import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.rotating.ProxyGatewayBackup;
import com.nattguld.http.proxies.rotating.RotatingProxy;
import com.nattguld.http.proxies.rotating.RotatingProxyBackup;
import com.nattguld.http.proxies.rotating.RotatingProxyManager;
import com.nattguld.http.proxies.standard.StandardProxyManager;
import com.nattguld.http.util.InternetConnectionType;

/**
 * 
 * @author randqm
 *
 */

public class ProxyConfig extends Config {
	
	/**
	 * Whether fiddler is used for network operations or not.
	 */
	private boolean fiddler;
	
	/**
	 * Whether cellular data is used or not.
	 */
	private boolean cellularMode;
	
	/**
	 * The datacenter proxy gateway.
	 */
	private ProxyGatewayBackup datacenterGateway;
	
	/**
	 * The residential proxy gateway.
	 */
	private ProxyGatewayBackup residentialGateway;
	
	/**
	 * The max. rotating datacenter proxy threads.
	 */
	private int maxRotatingDatacenterProxyThreads = 10;
	
	/**
	 * The max. rotating residential proxy threads.
	 */
	private int maxRotatingResidentialProxyThreads = 10;
	

	@Override
	protected void read(JsonReader reader) {
		this.fiddler = reader.getAsBoolean("fiddler", false);
		this.cellularMode = reader.has("4G_mode") ? reader.getAsBoolean("4G_mode") : reader.getAsBoolean("cellular_mode", false);
		
		if (reader.has("datacenter_gateway")) {
			this.datacenterGateway = (ProxyGatewayBackup)reader.getAsObject("datacenter_gateway", ProxyGatewayBackup.class, null);
			
			if (Objects.nonNull(datacenterGateway)) {
				for (RotatingProxyBackup rrp : datacenterGateway.getGateways()) {
					if (Objects.isNull(rrp)) {
						continue;
					}
					RotatingProxy rp = new RotatingProxy(InternetConnectionType.DATACENTER, rrp.getCooldown(), new LocalProxyConfig(datacenterGateway.getMaxParallel()), rrp.getHost(), rrp.getPort());
					RotatingProxyManager.getSingleton().add(rp);
				}
			}
			this.maxRotatingDatacenterProxyThreads = datacenterGateway.getMaxParallel();
			
		} else if (reader.has("max_rotating_dc_proxy_threads")) {
			this.maxRotatingDatacenterProxyThreads = reader.getAsInt("max_rotating_dc_proxy_threads", 10);
		}
		if (reader.has("residential_gateway")) {
			this.residentialGateway = (ProxyGatewayBackup)reader.getAsObject("residential_gateway", ProxyGatewayBackup.class, null);
			
			if (Objects.nonNull(residentialGateway)) {
				for (RotatingProxyBackup rrp : residentialGateway.getGateways()) {
					RotatingProxy rp = new RotatingProxy(InternetConnectionType.RESIDENTIAL, rrp.getCooldown(), new LocalProxyConfig(datacenterGateway.getMaxParallel()), rrp.getHost(), rrp.getPort());
					RotatingProxyManager.getSingleton().add(rp);
				}
			}
			this.maxRotatingResidentialProxyThreads = residentialGateway.getMaxParallel();
			
		} else if (reader.has("max_rotating_res_proxy_threads")) {
			this.maxRotatingResidentialProxyThreads = reader.getAsInt("max_rotating_res_proxy_threads", 10);
		}
		if (reader.has("imported_proxies")) { //TODO Deprecated
			List<HttpProxy> importedProxies = reader.getAsList("imported_proxies", new TypeToken<List<HttpProxy>>() {}.getType(), new ArrayList<HttpProxy>());
			
			for (HttpProxy proxy : importedProxies) {
				StandardProxyManager.getSingleton().add(proxy);
			}
		}
		save();
	}

	@Override
	protected void write(JsonWriter writer) {
		writer.write("fiddler", fiddler);
		writer.write("cellular_mode", cellularMode);
		writer.write("max_rotating_dc_proxy_threads", maxRotatingDatacenterProxyThreads);
		writer.write("max_rotating_res_proxy_threads", maxRotatingResidentialProxyThreads);
	}
	
	@Override
	protected String getSaveFileName() {
		return ".proxy_config";
	}
	
	/**
	 * Modifies the max. rotating datacenter proxy threads.
	 * 
	 * @param maxRotatingDatacenterProxyThreads The new max threads.
	 * 
	 * @return The config.
	 */
	public ProxyConfig setMaxRotatingDatacenterProxyThreads(int maxRotatingDatacenterProxyThreads) {
		this.maxRotatingDatacenterProxyThreads = maxRotatingDatacenterProxyThreads;
		return this;
	}
	
	/**
	 * Retrieves the max. rotating datacenter proxy threads.
	 * 
	 * @return The max threads.
	 */
	public int getMaxRotatingDatacenterProxyThreads() {
		return maxRotatingDatacenterProxyThreads;
	}
	
	/**
	 * Modifies the max. rotating residential proxy threads.
	 * 
	 * @param maxRotatingResidentialProxyThreads The new max threads.
	 * 
	 * @return The config.
	 */
	public ProxyConfig setMaxRotatingResidentialProxyThreads(int maxRotatingResidentialProxyThreads) {
		this.maxRotatingResidentialProxyThreads = maxRotatingResidentialProxyThreads;
		return this;
	}
	
	/**
	 * Retrieves the max. rotating residential proxy threads.
	 * 
	 * @return The max threads.
	 */
	public int getMaxRotatingResidentialProxyThreads() {
		return maxRotatingResidentialProxyThreads;
	}
	
	/**
	 * Modifies the cellularMode mode.
	 * 
	 * @param cellularMode The new mode.
	 * 
	 * @return The config.
	 */
	public ProxyConfig setCellularMode(boolean cellularMode) {
		this.cellularMode = cellularMode;
		return this;
	}
	
	/**
	 * Retrieves the cellularMode mode.
	 * 
	 * @return The cellularMode mode.
	 */
	public boolean isCellularMode() {
		return cellularMode;
	}
	
	/**
	 * Modifies whether fiddler is used or not.
	 * 
	 * @param fiddler The new state.
	 * 
	 * @return The config.
	 */
	public ProxyConfig setFiddler(boolean fiddler) {
		this.fiddler = fiddler;
		return this;
	}
	
	/**
	 * Retrieves whether fiddler is used or not.
	 * 
	 * @return The result.
	 */
	public boolean isFiddler() {
		return fiddler;
	}
	
	/**
	 * Retrieves the config.
	 * 
	 * @return The config.
	 */
	public static ProxyConfig getConfig() {
		return (ProxyConfig)ConfigManager.getConfig(new ProxyConfig());
	}

}
