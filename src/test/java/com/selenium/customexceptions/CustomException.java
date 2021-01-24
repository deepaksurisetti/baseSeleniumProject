package com.selenium.customexceptions;

/**
 * 
 * @author Deepak
 *
 */
@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

	private String fieldName = "";

	/**
	 * 
	 * @param errorDescription
	 */
	public CustomException(String errorDescription) {
		super(errorDescription);
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 
	 * @param fieldName
	 * @param errorDescription
	 */
	public CustomException(String fieldName, String errorDescription) {
		super(errorDescription);
		this.fieldName = fieldName;
	}
}
