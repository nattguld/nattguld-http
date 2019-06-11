package com.nattguld.http.proxies.rotating;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * @author randqm
 *
 */

public class ProxyUsageMonitor {
	
	/**
	 * The proxy uses.
	 */
	private static Map<RotatingProxy, List<ProxyUse>> proxyUses = new ConcurrentHashMap<>();
	
	
	/**
	 * Marks a proxy as in use by a user.
	 * 
	 * @param rp The proxy.
	 * 
	 * @param user The user.
	 * 
	 * @param inUse The use state.
	 */
	public static void setInUse(RotatingProxy rp, String user, boolean inUse) {
		updateUses(rp);
		
		if (!proxyUses.containsKey(rp)) {
			if (!inUse) {
				return;
			}
			proxyUses.put(rp, new CopyOnWriteArrayList<>());
		}
		ProxyUse use = getUseForUser(rp, user);
		
		if (Objects.isNull(use) && inUse) {
			proxyUses.get(rp).add(new ProxyUse(user));
			return;
		}
		if (!inUse) {
			use.deactivate();
			return;
		}
		use.updateLastUse();
	}
	
	/**
	 * Indicates a proxy was claimed but ended up being unused.
	 * 
	 * @param rp The rotating proxy.
	 * 
	 * @param user The user.
	 */
	public static void setUnused(RotatingProxy rp, String user) {
		ProxyUse use = getUseForUser(rp, user);
		
		if (Objects.nonNull(use)) {
			proxyUses.get(rp).remove(use);
		}
	}
	
	/**
	 * Retrieves whether a proxy is available for a user or not.
	 * 
	 * @param rp The proxy.
	 * 
	 * @param user The user.
	 * 
	 * @return The result.
	 */
	public static boolean isAvailable(RotatingProxy rp, String user) {
		if (!proxyUses.containsKey(rp)) {
			return true;
		}
		ProxyUse use = getUseForUser(rp, user);
		
		if (Objects.isNull(use)) {
			return true;
		}
		if (!use.isActive() && (System.currentTimeMillis() - use.getLastUse()) > rp.getCooldownMs()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves the remaining cooldown for a user on a proxy.
	 * 
	 * @param rp The proxy.
	 * 
	 * @param user The user.
	 * 
	 * @return The cooldown.
	 */
	public static long getCooldown(RotatingProxy rp, String user) {
		if (!proxyUses.containsKey(rp)) {
			return 0;
		}
		ProxyUse use = getUseForUser(rp, user);
		
		if (Objects.isNull(use)) {
			return 0;
		}
		long cd = (System.currentTimeMillis() - use.getLastUse());
		return cd > rp.getCooldownMs() ? 0 : (rp.getCooldownMs() - cd);
	}
	
	/**
	 * Retrieves the active uses of a proxy.
	 * 
	 * @param rp The proxy.
	 * 
	 * @return The active uses.
	 */
	public static int getActiveUses(RotatingProxy rp) {
		if (!proxyUses.containsKey(rp)) {
			return 0;
		}
		int count = 0;
		
		for (ProxyUse use : getUses(rp)) {
			if (use.isActive()) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Updates the uses for a proxy.
	 * 
	 * @param rp The proxy.
	 */
	private static void updateUses(RotatingProxy rp) {
		if (!proxyUses.containsKey(rp)) {
			return;
		}
		for (ProxyUse use : getUses(rp)) {
			if (!use.isActive() && (System.currentTimeMillis() - use.getLastUse()) > rp.getCooldownMs()) {
				proxyUses.get(rp).remove(use);
			}
		}
		if (proxyUses.get(rp).isEmpty()) {
			proxyUses.remove(rp);
		}
	}
	
	/**
	 * Retrieves the use for a proxy and user.
	 * 
	 * @param rp The proxy.
	 * 
	 * @param user The user.
	 * 
	 * @return The use.
	 */
	private static ProxyUse getUseForUser(RotatingProxy rp, String user) {
		if (!proxyUses.containsKey(rp)) {
			return null;
		}
		for (ProxyUse pu : getUses(rp)) {
			if (pu.getUser().equals(user)) {
				return pu;
			}
		}
		return null;
	}
	
	/**
	 * Retrieves the uses for a proxy.
	 * 
	 * @param rp The proxy.
	 * 
	 * @return The uses.
	 */
	private static List<ProxyUse> getUses(RotatingProxy rp) {
		return proxyUses.get(rp);
	}

}
