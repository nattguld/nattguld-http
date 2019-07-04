package com.nattguld.http.util.html;

/**
 * 
 * @author randqm
 *
 */

public class ExtractionResponse<T extends Object> {
	
	/**
	 * The extracted element.
	 */
	private T element;
	
	/**
	 * The error message if any.
	 */
	private String errorMessage = "Unexpected error";
	
	/**
	 * Whether an element was assigned or not.
	 */
	private boolean assigned;
	
	
	/**
	 * Modifies the element.
	 * 
	 * @param element The new element.
	 * 
	 * @return The extraction response.
	 */
	public ExtractionResponse<T> setElement(T element) {
		this.element = element;
		this.assigned = true;
		return this;
	}
	
	/**
	 * Retrieves the element.
	 * 
	 * @return The element.
	 */
	public T getElement() {
		return element;
	}
	
	/**
	 * Modifies the error message.
	 * 
	 * @param errorMessage The new error message.
	 * 
	 * @return The extraction response.
	 */
	public ExtractionResponse<T> setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		this.assigned = false;
		return this;
	}
	
	/**
	 * Retrieves the error message.
	 * 
	 * @return The error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Retrieves whether an element was assigned or not.
	 * 
	 * @return The result.
	 */
	public boolean isAssigned() {
		return assigned;
	}

}
