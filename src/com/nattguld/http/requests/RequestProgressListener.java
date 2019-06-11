package com.nattguld.http.requests;

/**
 * 
 * @author randqm
 *
 */

public abstract class RequestProgressListener {

	/**
	 * The current progress.
	 */
	private int progress = -1;
	
	
	/**
	 * Handles actions on progress change.
	 * 
	 * @param progress The progress.
	 */
	protected abstract void onChange(int progress);
	
	/**
	 * Modifies the progress.
	 * 
	 * @param progress The new progress.
	 */
	public void setProgress(int progress) {
		if (this.progress == progress) {
			return;
		}
		this.progress = progress;
		onChange(progress);
	}

}
