package com.nattguld.http.browser;

import com.nattguld.http.cfg.HttpVersion;
import com.nattguld.http.cfg.NetConfig;
import com.nattguld.http.util.NetUtil;
import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public class Browser {
	
	/**
	 * Whether we're on mobile or not.
	 */
	private boolean mobile;
	
	/**
	 * The user agent.
	 */
	private String userAgent;
	
	/**
	 * The screen resolution.
	 */
	private int[] screenResolution;
	
	/**
	 * The available screen resolution.
	 */
	private int[] availableScreenResolution;
	
	/**
	 * The cpu cores.
	 */
	private int cpuCores;
	
	/**
	 * The language.
	 */
	private String language;
	
	/**
	 * The read timeout.
	 */
	private int readTimeout;
	
	/**
	 * The connection timeout.
	 */
	private int connectionTimeout;
	
	/**
	 * Whether to cache or not.
	 */
	private boolean cache;
	
	/**
	 * Whether to send a do-not-track request or not.
	 */
	private boolean doNotTrack;
	
	/**
	 * Whether to debug or not.
	 */
	private boolean debug;
	
	/**
	 * The HTTP version.
	 */
	private HttpVersion httpVersion;
	
	/**
	 * The maximum amount of connection attempts.
	 */
	private transient int connectionAttempts;
	
	
	/**
	 * Creates a new browser.
	 */
	public Browser() {
		this(false);
	}
	/**
	 * Creates a new browser.
	 * 
	 * @param mobile Whether it's a mobile device or not.
	 */
	public Browser(boolean mobile) {
		this(mobile, mobile ? UserAgents.getMobileUserAgent() : UserAgents.getDesktopUserAgent());
	}
	
	/**
	 * Creates a new browser.
	 * 
	 * @param mobile Whether it's a mobile device or not.
	 * 
	 * @param userAgent The user agent.
	 */
	public Browser(boolean mobile, String userAgent) {
		this.mobile = mobile;
		this.userAgent = userAgent;
		this.screenResolution = mobile ? NetUtil.getMobileScreenResolution() : NetUtil.getDesktopScreenResolution();
		this.availableScreenResolution = new int[] {!mobile && Maths.random(4) != 1 ? (screenResolution[0] - 64) : screenResolution[0], screenResolution[1]};
		this.cpuCores = mobile ? NetUtil.getMobileCores() : NetUtil.getDesktopCores();
		this.language = NetUtil.DEFAULT_BROWSER_LANGUAGE;
		this.readTimeout = NetConfig.getConfig().getReadTimeout();
		this.connectionTimeout = NetConfig.getConfig().getConnectionTimeout();
		this.cache = false;
		this.doNotTrack = NetConfig.getConfig().isDoNotTrack();
		this.debug = NetConfig.getConfig().isDebug();
		this.httpVersion = HttpVersion.HTTP_1_1;
		this.connectionAttempts = 6;
	}
	
	/**
	 * Modifies the HTTP version.
	 * 
	 * @param httpVersion The new HTTP version.
	 */
	public void setHttpVersion(HttpVersion httpVersion) {
		this.httpVersion = httpVersion;
	}
	
	/**
	 * Retrieves the HTTP version.
	 * 
	 * @return The HTTP version.
	 */
	public HttpVersion getHttpVersion() {
		return httpVersion;
	}
	
	/**
	 * Modifies whether the browser is mobile or not.
	 * 
	 * @param mobile The new state.
	 */
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * Retrieves whether the browser is mobile or not.
	 * 
	 * @return The result.
	 */
	public boolean isMobile() {
		return mobile;
	}
	
	/**
	 * Modifies the user agent.
	 * 
	 * @param userAgent The new user agent.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	/**
	 * Retrieves the user agent.
	 * 
	 * @return The user agent.
	 */
	public String getUserAgent() {
		return userAgent;
	}
	
	/**
	 * Modifies the screen width.
	 * 
	 * @param screenWidth The new screen width.
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenResolution[0] = screenWidth;
	}
	
	/**
	 * Retrieves the screen width.
	 * 
	 * @return The screen width.
	 */
	public int getScreenWidth() {
		return screenResolution[0];
	}
	
	/**
	 * Modifies the screen height.
	 * 
	 * @param screenWidth The new screen height.
	 */
	public void setScreenHeight(int screenHeight) {
		this.screenResolution[1] = screenHeight;
	}
	
	/**
	 * Retrieves the screen height.
	 * 
	 * @return The screen height.
	 */
	public int getScreenHeight() {
		return screenResolution[1];
	}
	
	/**
	 * Modifies the available screen width.
	 * 
	 * @param availableScreenWidth The new available screen width.
	 */
	public void setAvailableScreenWith(int availableScreenWidth) {
		this.availableScreenResolution[0] = availableScreenWidth;
	}
	
	/**
	 * Retrieves the available screen width.
	 * 
	 * @return The available screen width.
	 */
	public int getAvailableScreenWidth() {
		return availableScreenResolution[0];
	}
	
	/**
	 * Modifies the available screen height.
	 * 
	 * @param availableScreenHeight The new available screen height.
	 */
	public void setAvailableScreenHeight(int availableScreenHeight) {
		this.availableScreenResolution[1] = availableScreenHeight;
	}
	
	/**
	 * Retrieves the available screen height.
	 * 
	 * @return The available screen height.
	 */
	public int getAvailableScreenHeight() {
		return availableScreenResolution[1];
	}
	
	/**
	 * Modifies the CPU cores.
	 * 
	 * @param cpuCores The new CPU cores.
	 */
	public void setCPUCores(int cpuCores) {
		this.cpuCores = cpuCores;
	}
	
	/**
	 * Retrieves the CPU cores.
	 * 
	 * @return The CPU cores.
	 */
	public int getCPUCores() {
		return cpuCores;
	}
	
	/**
	 * Modifies the language.
	 * 
	 * @param language The new language.
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * Retrieves the language.
	 * 
	 * @return The language.
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * Modifies the read timeout.
	 * 
	 * @param readTimeout The new read timeout.
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	/**
	 * Retrieves the read timeout.
	 * 
	 * @return The read timeout.
	 */
	public int getReadTimeout() {
		return readTimeout;
	}
	
	/**
	 * Modifies the connection time out.
	 * 
	 * @param connectionTimeout The new connection timeout.
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	/**
	 * Retrieves the connection timeout.
	 * 
	 * @return The connection timeout.
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	
	/**
	 * Modifies whether to use cache or not.
	 * 
	 * @param cache The new state.
	 */
	public void setUseCache(boolean cache) {
		this.cache = cache;
	}
	
	/**
	 * Retrieves whether to use caching or not.
	 * 
	 * @return The result.
	 */
	public boolean useCache() {
		return cache;
	}
	
	/**
	 * Modifies the do-not-track preference.
	 * 
	 * @param doNotTrack The new preference.
	 */
	public void setDoNotTrack(boolean doNotTrack) {
		this.doNotTrack = doNotTrack;
	}
	
	/**
	 * Retrieves whether to use a do-not-track request.
	 * 
	 * @return The result.
	 */
	public boolean isDoNotTrack() {
		return doNotTrack;
	}
	
	/**
	 * Modifies whether to debug or not.
	 * 
	 * @param debug The new state.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Whether to debug or not.
	 * @return
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Modifies the amount of connection attempts.
	 * 
	 * @param connectionAttempts The new amount.
	 */
	public void setConnectionAttempts(int connectionAttempts) {
		this.connectionAttempts = connectionAttempts;
	}
	
	/**
	 * The maximum amount of connection attempts.
	 * 
	 * @return The result.
	 */
	public int getConnectionAttempts() {
		return connectionAttempts;
	}
	
	/**
	 * Retrieves a clone of this browser.
	 */
	public Browser clone() {
		Browser browser = new Browser(isMobile(), getUserAgent());
		browser.setAvailableScreenHeight(getAvailableScreenHeight());
		browser.setAvailableScreenWith(getAvailableScreenWidth());
		browser.setConnectionTimeout(getConnectionTimeout());
		browser.setCPUCores(getCPUCores());
		browser.setDebug(isDebug());
		browser.setDoNotTrack(isDoNotTrack());
		browser.setLanguage(getLanguage());
		browser.setMobile(isMobile());
		browser.setReadTimeout(getReadTimeout());
		browser.setScreenHeight(getScreenHeight());
		browser.setScreenWidth(getScreenWidth());
		browser.setUseCache(useCache());
		browser.setUserAgent(getUserAgent());
		browser.setConnectionAttempts(getConnectionAttempts());
		return browser;
	}

}
