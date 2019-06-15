package com.nattguld.http.proxies.cfg;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.nattguld.data.cfg.Config;
import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;
import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.rotating.ProxyGateway;

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
	private ProxyGateway datacenterGateway;
	
	/**
	 * The residential proxy gateway.
	 */
	private ProxyGateway residentialGateway;
	
	/**
	 * Holds the imported proxies.
	 */
	private List<HttpProxy> importedProxies = new ArrayList<>();
	

	@Override
	protected void read(JsonReader reader) {
		this.fiddler = reader.getAsBoolean("fiddler", false);
		this.cellularMode = reader.has("4G_mode") ? reader.getAsBoolean("4G_mode") : reader.getAsBoolean("cellular_mode", false);
		this.datacenterGateway = (ProxyGateway)reader.getAsObject("datacenter_gateway", ProxyGateway.class, null);
		this.residentialGateway = (ProxyGateway)reader.getAsObject("residential_gateway", ProxyGateway.class, null);
		this.importedProxies = reader.getAsList("imported_proxies", new TypeToken<List<HttpProxy>>() {}.getType(), new ArrayList<HttpProxy>());
	}

	@Override
	protected void write(JsonWriter writer) {
		writer.write("fiddler", fiddler);
		writer.write("cellular_mode", cellularMode);
		writer.write("datacenter_gateway", datacenterGateway);
		writer.write("residential_gateway", residentialGateway);
		writer.write("imported_proxies", importedProxies);
	}
	
	@Override
	protected String getSaveFileName() {
		return ".proxy_config";
	}
	
	/**
	 * Modifies the cellularMode mode.
	 * 
	 * @param cellularMode The new mode.
	 */
	public void setCellularMode(boolean cellularMode) {
		this.cellularMode = cellularMode;
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
	 */
	public void setFiddler(boolean fiddler) {
		this.fiddler = fiddler;
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
	 * Modifies the datacenter gateway.
	 * 
	 * @param datacenterGateway The new gateway.
	 */
	public void setDatacenterGateway(ProxyGateway datacenterGateway) {
		this.datacenterGateway = datacenterGateway;
	}
	
	/**
	 * Retrieves the datacenter gateway.
	 * 
	 * @return The gateway.
	 */
	public ProxyGateway getDatacenterGateway() {
		return datacenterGateway;
	}
	
	/**
	 * Modifies the residential gateway.
	 * 
	 * @param residentialGateway The new gateway.
	 */
	public void setResidentialGateway(ProxyGateway residentialGateway) {
		this.residentialGateway = residentialGateway;
	}
	
	/**
	 * Retrieves the residential gateway.
	 * 
	 * @return The gateway.
	 */
	public ProxyGateway getResidentialGateway() {
		return residentialGateway;
	}
	
	/**
	 * Imports a proxy.
	 * 
	 * @param proxy The new proxy.
	 */
	public void importProxy(HttpProxy proxy) {
		importedProxies.add(proxy);
	}
	
	/**
	 * Removes an imported proxy.
	 * 
	 * @param proxy The proxy to remove.
	 */
	public void removeImportedProxy(HttpProxy proxy) {
		importedProxies.remove(proxy);
	}
	
	/**
	 * Retrieves the imported proxies.
	 * 
	 * @return The imported proxies.
	 */
	public List<HttpProxy> getImportedProxies() {
		return importedProxies;
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
