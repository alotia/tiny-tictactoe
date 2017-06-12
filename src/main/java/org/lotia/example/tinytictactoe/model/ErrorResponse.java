package org.lotia.example.tinytictactoe.model;

/*
 * This class is used to create an ErrorResponse Object when returning an error to the client
 */
public class ErrorResponse {
	private int errorCode;
	private String message;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
