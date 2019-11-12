package com.nattguld.http.proxies.rotating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import com.nattguld.http.proxies.HttpProxy;
import com.nattguld.util.Misc;

/**
 * 
 * @author randqm
 *
 */

@Deprecated
public class ProxyGatewayBackup {
	
	/**
	 * The maximum parallel connections.
	 */
	private int maxParallel;
	
	/**
	 * The proxy gateways.
	 */
	private final List<RotatingProxyBackup> gateways;
	
	
	/**
	 * The maximum amount of parallel connections.
	 * 
	 * @param maxParallel
	 */
	public ProxyGatewayBackup(int maxParallel) {
		this.maxParallel = maxParallel;
		this.gateways = new CopyOnWriteArrayList<>();
	}
	/**
	 * Adds a new gateway.
	 * 
	 * @param gateway The new gateway.
	 */
	public void add(RotatingProxyBackup gateway) {
		gateways.add(gateway);
	}
	
	/**
	 * Removes the gateways.
	 * 
	 * @param gateway The gateways.
	 */
	public void remove(HttpProxy gateway) {
		if (contains(gateway)) {
			gateways.remove(gateway);
		}
	}
	
	/**
	 * Retrieves whether the gateway contains a proxy or not.
	 * 
	 * @param proxy The proxy.
	 * 
	 * @return The result.
	 */
	public boolean contains(HttpProxy proxy) {
		if (Objects.isNull(proxy) || !(proxy instanceof RotatingProxyBackup)) {
			return false;
		}
		return gateways.contains(proxy);
	}
	
	/**
	 * The max. amount of parallel connections.
	 * 
	 * @return The max. amount.
	 */
	public int getMaxParallel() {
		return maxParallel;
	}
	
	/**
	 * Retrieves the gateways.
	 * 
	 * @return The gateways.
	 */
	public List<RotatingProxyBackup> getGateways() {
		return gateways;
	}

}
