package com.nattguld.http.proxies.cfg;

import java.util.ArrayList;
import java.util.List;

import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.http.proxies.rotating.ProxyGateway;

/**
 * 
 * @author randqm
 *
 */

public class ProxyConfigs {
	
	/**
	 * Whether fiddler is used for network operations or not.
	 */
	private boolean fiddler;
	
	/**
	 * The 4G mode.
	 */
	private boolean fourGMode;
	
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
	
	
	/**
	 * Modifies the 4G mode.
	 * 
	 * @param fourGMode The new mode.
	 */
	public void set4GMode(boolean fourGMode) {
		this.fourGMode = fourGMode;
	}
	
	/**
	 * Retrieves the 4G mode.
	 * 
	 * @return The 4G mode.
	 */
	public boolean is4GMode() {
		return fourGMode;
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

}
