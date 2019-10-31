package com.nattguld.http.pooling;

import com.nattguld.http.proxies.cfg.ProxyConfig;
import com.nattguld.util.pooling.ObjectPool;

/**
 * 
 * @author randqm
 *
 */

public class ProxyPool extends ObjectPool {

	
	public ProxyPool(int maxPoolSize) {
		super(maxPoolSize);
	}

	@Override
	protected Object createElement() {
		// TODO Auto-generated method stub
		return null;
	}

}
