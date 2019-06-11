package com.nattguld.http.browser;

import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class UserAgents {
	
	/**
	 * The available desktop user agents.
	 */
	private static final String[] DESKTOP_USER_AGENTS = {
			//Chrome OS
			"Mozilla/5.0 (X11; CrOS x86_64 11021.56.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.76 Safari/537.36",
			"Mozilla/5.0 (X11; CrOS x86_64 11021.81.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36",
			
			//Linux
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36",
			"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0",
			
			//Mac Os X
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.1.2 Safari/603.3.8",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/601.7.8 (KHTML, like Gecko) Version/9.1.3 Safari/537.86.7",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:45.0) Gecko/20100101 Firefox/45.0",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1.2 Safari/605.1.15",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/603.2.5 (KHTML, like Gecko) Version/10.1.1 Safari/603.2.5",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36",
    		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/601.7.7 (KHTML, like Gecko) Version/9.1.2 Safari/601.7.7",
    		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.1.2 Safari/603.3.8",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:63.0) Gecko/20100101 Firefox/63.0",
			
			//Windows
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393",
			"Mozilla/5.0 (Windows NT 5.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0",
			"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0",
			"Mozilla/5.0 (Windows NT 10.0; rv:63.0) Gecko/20100101 Firefox/63.0",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:63.0) Gecko/20100101 Firefox/63.0",
			"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:63.0) Gecko/20100101 Firefox/63.0",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299",
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063",
    		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393",
    		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063",
    		"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393",
    		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299",
    		"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063",
    		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134",
	};
	
	/**
	 * The possible android versions.
	 */
	private static final Object[][] ANDROID_VERSIONS = {
			//Version - Range start, Market share (6 May 2019)
			{"9", 3.0},
			{"8.1.0", 12.0},
			{"8.0.0", 21.0},
			{"7.1.2", 8.0},
			{"7.1.1", 2.0},
			{"7.0", 14.0},
			{"6.0.1", 19.5},
			{"6.0", 3.0},
			{"5.1.1", 10.0},
			{"5.0.2", 0.5},
			{"5.0.1", 0.5},
			{"5.0", 1.5},
			{"4.4.4", 5.0},
	};
	
	/**
	 * The possible ios versions.
	 */
	private static final Object[][] IOS_VERSION = {
			{"12_2", 86.2},
			{"11_4_1", 7.4},
			{"10_3_3", 2.3},
			{"9_3_5", 4.0},
	};
	
	/**
	 * The market share of major phone brands in europe/usa
	 */
	private static final Object[][] BRAND_MARKET_SHARES = {
			{PhoneBrand.SAMSUNG, 40.0},
			{PhoneBrand.APPLE, 40.0},
			{PhoneBrand.HUAWEI, 7.0},
			{PhoneBrand.XIAOMI, 1.0},
			{PhoneBrand.LG, 4.0},
			{PhoneBrand.MOTOROLA, 2.0},
			{PhoneBrand.HTC, 3.0},
			{PhoneBrand.PIXEL, 3.0},
	};
	
	/**
	 * The possible browser brands.
	 */
	private static final Object[][] BROWSER_BRANDS = {
			{BrowserBrand.CHROME, 94.0},
			{BrowserBrand.FIREFOX, 6.0},
			//TODO opera, uc browser, samsung browser
	};
	
	/**
	 * Retrieves a random mobile user agent.
	 * 
	 * @return The random mobile user agent.
	 */
	public static String getMobileUserAgent() {
		PhoneBrand brand = getRandomPhoneBrand();
		
		if (brand == PhoneBrand.APPLE) {
			return "Mozilla/5.0 (iPhone; CPU iPhone OS " + getRandomIOSVersion() 
				+ " like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/" 
				+ BrowserBrand.SAFARI.getRandomVersion() + " Mobile/15E148 Safari/604.1";
		}
		String osVersion = getRandomAndroidVersion();
		BrowserBrand browser = getRandomBrowserBrand();
		String browserVersion = browser.getRandomVersion();

		if (browser == BrowserBrand.FIREFOX) {
			return "Mozilla/5.0 (Android " + osVersion + "; Mobile; rv:" + browserVersion + ") Gecko/" + browserVersion + " Firefox/" + browserVersion;
		}
		String modelName = brand.getRandomModel(osVersion);
		return "Mozilla/5.0 (Linux; Android " + osVersion + "; " + modelName + " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/" + browserVersion + " Mobile Safari/537.36";
	}
	
    /**
     * Retrieves a random desktop user agent.
     * 
     * @return The user agent.
     */
    public static String getDesktopUserAgent() {
    	return DESKTOP_USER_AGENTS[Maths.random(DESKTOP_USER_AGENTS.length)];
    }
    
    /**
     * Retrieves a random user agent.
     * 
     * @return The user agent.
     */
    public static String getMixedUserAgent() {
    	return Maths.random(2) == 0 ? getMobileUserAgent() : getDesktopUserAgent();
    }
	
	/**
	 * Retrieves a random browser version.
	 * 
	 * @return The browser version.
	 */
	public static BrowserBrand getRandomBrowserBrand() {
		double marker = 0.0;
		final int roll = Maths.random(100);
		
		for (int i = 0; i < BROWSER_BRANDS.length; i++) {
			BrowserBrand brand = (BrowserBrand)BROWSER_BRANDS[i][0];
			double share = (double)BROWSER_BRANDS[i][1];
			
			if (roll > marker && roll <= (marker + share)) {
				return brand;
			}
			marker += share;
		}
		return BrowserBrand.CHROME;
	}
	
	/**
	 * Retrieves a random phone brand.
	 * 
	 * @return The phone brand.
	 */
	public static PhoneBrand getRandomPhoneBrand() {
		return getRandomPhoneBrand(true);
	}
	
	/**
	 * Retrieves a random phone brand.
	 * 
	 * @param includeApple Whether to include apple or not.
	 * 
	 * @return The phone brand.
	 */
	public static PhoneBrand getRandomPhoneBrand(boolean includeApple) {
		double marker = 0.0;
		final int roll = Maths.random(100);
		
		for (int i = 0; i < BRAND_MARKET_SHARES.length; i++) {
			PhoneBrand brand = (PhoneBrand)BRAND_MARKET_SHARES[i][0];
			double share = (double)BRAND_MARKET_SHARES[i][1];
			
			if (roll > marker && roll <= (marker + share)) {
				return brand;
			}
			marker += share;
		}
		return PhoneBrand.APPLE;
	}
	
	/**
	 * Retrieves a random android version.
	 * 
	 * @return The android version.
	 */
	public static String getRandomAndroidVersion() {
		double marker = 0.0;
		final int roll = Maths.random(100);
		
		for (int i = 0; i < ANDROID_VERSIONS.length; i++) {
			String version = (String)ANDROID_VERSIONS[i][0];
			double share = (double)ANDROID_VERSIONS[i][1];
			
			if (roll > marker && roll <= (marker + share)) {
				return version;
			}
			marker += share;
		}
		return "6.0"; //Current most common
	}
	
	/**
	 * Retrieves a random ios version.
	 * 
	 * @return The ios version.
	 */
	public static String getRandomIOSVersion() {
		double marker = 0.0;
		final int roll = Maths.random(100);
		
		for (int i = 0; i < IOS_VERSION.length; i++) {
			String version = (String)IOS_VERSION[i][0];
			double share = (double)IOS_VERSION[i][1];
			
			if (roll > marker && roll <= (marker + share)) {
				return version;
			}
			marker += share;
		}
		return "12.2"; //Most recent/common
	}

}
