package com.nattguld.http;

import java.math.BigDecimal;

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
	 * Modifies the down data.
	 * 
	 * @param down The new amount.
	 * 
	 * @return The down data.
	 */
	public long setDown(long down) {
		this.down = down;
		return down;
	}
	
	/**
	 * Modifies the up data.
	 * 
	 * @param up The new amount.
	 * 
	 * @return The up data.
	 */
	public long setUp(long up) {
		this.up = up;
		return up;
	}
	
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
	
	/**
	 * Retrieves the readable format for a given amount of bytes.
	 * 
	 * @param bytes The bytes.
	 * 
	 * @return The readable format.
	 */
	public static String getReadableFormat(long bytes) {
		BigDecimal bytesBd = new BigDecimal(bytes);
				
		if (bytesBd.longValue() < 1024) {
			return bytesBd.longValue() + " b";
		}
		BigDecimal kiloBytes = new BigDecimal((bytesBd.doubleValue() / (double)1024));
		
		if (bytesBd.longValue() < 1048576) {
			return kiloBytes.longValue() + " Kb";
		}
		return new BigDecimal((kiloBytes.doubleValue() / (double)1024)).longValue() + " Mb";
	}

}
