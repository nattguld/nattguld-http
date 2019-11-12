package com.nattguld.http.proxies.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author randqm
 *
 */

public class LocalProxyConfig {
	
	/**
	 * The default connection limit.
	 */
	private static final int DEFAULT_CONNECTION_LIMIT = 5;
	
	/**
	 * The current users of the proxy.
	 */
	private final transient List<String> users;
	
	/**
	 * The limit on how many simultaneous connections can use the proxy.
	 */
	private int connectionLimit;
	
	
	/**
	 * Creates a new local proxy config.
	 */
	public LocalProxyConfig() {
		this(DEFAULT_CONNECTION_LIMIT);
	}
	
	/**
	 * Creates a new local proxy config.
	 * 
	 * @param connectionLimit The connection limit.
	 */
	public LocalProxyConfig(int connectionLimit) {
		this.connectionLimit = connectionLimit;
		this.users = new ArrayList<>();
	}
	
	/**
	 * Adds a new user.
	 * 
	 * @param user The new user.
	 * 
	 * @return The config.
	 */
	public LocalProxyConfig addUser(String user) {
		users.add(user);
		return this;
	}
	
	/**
	 * Retrieves whether a given user is present or not.
	 * 
	 * @param user The user.
	 * 
	 * @return The result.
	 */
	public boolean hasUser(String user) {
		if (Objects.isNull(user)) {
			return false;
		}
		return users.contains(user);
	}
	
	/**
	 * Removes a user.
	 * 
	 * @param user the user.
	 * 
	 * @return The config.
	 */
	public LocalProxyConfig removeUser(String user) {
		users.remove(user);
		return this;
	}
	
	/**
	 * Retrieves whether there's room for new users or not.
	 * 
	 * @return The result.
	 */
	protected boolean hasRoom() {
		return users.size() < connectionLimit;
	}
	
	/**
	 * Retrieves whether a user can be added or not.
	 * 
	 * @return The result.
	 */
	public boolean canAddUser(String user, boolean unique) {
		return hasRoom() && (!unique || !hasUser(user));
	}
	
	/**
	 * Retrieves the amount of duplicate users.
	 * 
	 * @param user The user.
	 * 
	 * @return The amount.
	 */
	public int getDuplicateUserCount(String user) {
		int count = 0;
		
		for (String o : users) {
			if (o.equals(user)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Modifies the connection limit.
	 * 
	 * @param connectionLimit The new connection limit.
	 * 
	 * @return The config.
	 */
	public LocalProxyConfig setConnectionLimit(int connectionLimit) {
		this.connectionLimit = connectionLimit;
		return this;
	}
	
	/**
	 * Retrieves the connection limit.
	 * 
	 * @return The connection limit.
	 */
	public int getConnectionLimit() {
		return connectionLimit;
	}

	/**
	 * Retrieves the amount of connections in use.
	 * 
	 * @return The amount of connections.
	 */
	public int getConnectionsInUse() {
		return users.size();
	}

}
