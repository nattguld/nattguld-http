package com.nattguld.http.browser;

import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public enum BrowserBrand {
	
	SAFARI("Safari", new Object[][] {
		{"13.0", 70.0},
		{"12.0", 20.0},
		{"11.0", 5.0},
		{"10.0", 5.0},
	}),
	CHROME("Chrome", new Object[][] {
		{"79.0.3945.88", 27.0},
		{"78.0.3904.108", 2.0},
		{"77.0.3865.90", 2.0},
		{"76.0.3809.132", 2.0},
		{"75.0.3770.142", 4.0},
		{"75.0.3770.100", 1.0},
		{"74.0.3729.169", 18.0},
		{"74.0.3729.157", 2.0},
		{"74.0.3729.136", 1.0},
		{"74.0.3729.125", 1.0},
		{"74.0.3729.131", 1.0},
		{"74.0.3729.112", 1.0},
		{"73.0.3683.90", 11.0},
		{"72.0.3626.105", 6.5},
		{"72.0.3626.96", 1.0},
		{"72.0.3626.76", 0.5},
		{"71.0.3578.99", 5.0},
		{"71.0.3578.98", 1.0},
		{"71.0.3578.83", 1.0},
		{"70.0.3538.110", 4.0},
		{"70.0.3538.80", 1.0},
		{"70.0.3538.64", 1.0},
		{"69.0.3497.100", 3.0},
		{"69.0.3497.91", 1.0},
		{"69.0.3497.86", 1.0},
		{"69.0.3497.76", 0.5},
		{"68.0.3440.91", 0.5},
	}),
	FIREFOX("Firefox", new Object[][] {
		{"71.0", 80.0},
		{"70.0", 1.0},
		{"69.0", 1.0},
		{"68.0", 1.0},
		{"67.0", 1.0},
		{"66.0", 1.0},
		{"55.0", 15.0},
	});
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The versions.
	 */
	private final Object[][] versions;
	
	
	/**
	 * Creates a new browser brand.
	 * 
	 * @param name The name.
	 * 
	 * @param versions The versions.
	 */
	private BrowserBrand(String name, Object[][] versions) {
		this.name = name;
		this.versions = versions;
	}
	
	/**
	 * Retrieves a random version.
	 * 
	 * @return The version.
	 */
	public String getRandomVersion() {
		double marker = 0.0;
		final int roll = Maths.random(100);
		
		for (int i = 0; i < versions.length; i++) {
			String version = (String)versions[i][0];
			double share = (double)versions[i][1];
			
			if (roll > marker && roll <= (marker + share)) {
				return version;
			}
			marker += share;
		}
		return "71.0";
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the versions.
	 * 
	 * @return The versions.
	 */
	public Object[][] getVersions() {
		return versions;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
