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

public class ProxyGateway {
	
	/**
	 * The maximum parallel connections.
	 */
	private int maxParallel;
	
	/**
	 * The proxy gateways.
	 */
	private final List<RotatingProxy> gateways;
	
	
	/**
	 * The maximum amount of parallel connections.
	 * 
	 * @param maxParallel
	 */
	public ProxyGateway(int maxParallel) {
		this.maxParallel = maxParallel;
		this.gateways = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * Retrieves the next available proxy gateway.
	 * 
	 * @param user The user.
	 * 
	 * @param ignoreCooldowns Whether to ignore cooldowns or not.
	 * 
	 * @return The proxy.
	 */
	public RotatingProxy getNext(String user, boolean ignoreUser, boolean ignoreCooldowns) {
		if (gateways.isEmpty()) {
			System.err.println("No rotating proxies provided");
			return null;
		}
		if (getThreadsInUse() >= getMaxParallel()) {
			Misc.sleep(2000);
			return getNext(user, ignoreUser, ignoreCooldowns);
		}
		List<RotatingProxy> available = new ArrayList<>();
		
		for (RotatingProxy rp : gateways) {
			if (!ignoreUser && !ProxyUsageMonitor.isAvailable(rp, user)) {
				continue;
			}
			available.add(rp);
		}
		if (available.isEmpty()) {
			Misc.sleep(2000);
			return getNext(user, ignoreUser, ignoreCooldowns);
		}
		Collections.sort(available, new Comparator<RotatingProxy>() {
			@Override
			public int compare(RotatingProxy o1, RotatingProxy o2) {
		    	Boolean onCd1 = ProxyUsageMonitor.isAvailable(o1, user);
		    	Boolean onCd2 = ProxyUsageMonitor.isAvailable(o2, user);
		    	
		    	int compareOnCd = onCd1.compareTo(onCd2);

		    	if (compareOnCd != 0) {
		    		return compareOnCd;
		    	}
		    	Integer cd1 = o1.getCooldown();
		    	Integer cd2 = o2.getCooldown();
		    	
		    	return cd1.compareTo(cd2);
			}
		});
		RotatingProxy first = available.get(0);

		if (ignoreCooldowns || ProxyUsageMonitor.isAvailable(first, user)) {
			return first;
		}
		long waitTime = ProxyUsageMonitor.getCooldown(first, user);
		
		if (waitTime > 0) {
			System.err.println(user + " => Sleeping for " + (waitTime / 1000) + "s");
			Misc.sleep(waitTime);
		}
		while (getThreadsInUse() >= getMaxParallel()) {
			Misc.sleep(2000);
		}
		return first;
	}
	
	/**
	 * Adds a new gateway.
	 * 
	 * @param gateway The new gateway.
	 */
	public void add(RotatingProxy gateway) {
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
		if (Objects.isNull(proxy) || !(proxy instanceof RotatingProxy)) {
			return false;
		}
		return gateways.contains(proxy);
	}
	
	/**
	 * Retrieves the amount of threads in use.
	 * 
	 * @return The amount.
	 */
	public int getThreadsInUse() {
		int count = 0;
		
		for (RotatingProxy rp : gateways) {
			count += ProxyUsageMonitor.getActiveUses(rp);
		}
		return count;
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
	public List<RotatingProxy> getGateways() {
		return gateways;
	}

}
