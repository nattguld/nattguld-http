package com.nattguld.http;

/**
 * 
 * @author randqm
 *
 */

public class DataCounter {
	
	/**
	 * The data downloaded.
	 */
	private long down;
	
	/**
	 * The data uploaded.
	 */
	private long up;
	
	
	/**
	 * Adds downloaded data.
	 * 
	 * @param amount The amount.
	 * 
	 * @return The total amount downloaded.
	 */
	public long addDown(long amount) {
		this.down += amount;
		return down;
	}
	
	/**
	 * Adds uploaded data.
	 * 
	 * @param amount The amount.
	 * 
	 * @return The total amount uploaded.
	 */
	public long addUp(long amount) {
		this.up += amount;
		return up;
	}
	
	/**
	 * Retrieves the downloaded data.
	 * 
	 * @return The downloaded data.
	 */
	public long getDown() {
		return down;
	}
	
	/**
	 * Retrieves the uploaded data.
	 * 
	 * @return The uploaded data.
	 */
	public long getUp() {
		return up;
	}
	
	/**
	 * Resets the counters.
	 */
	public void reset() {
		this.down = 0L;
		this.up = 0L;
	}

}
