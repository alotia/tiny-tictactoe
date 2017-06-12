package org.lotia.example.tinytictactoe.exceptions;

import org.springframework.http.HttpStatus;

public class GameException extends RuntimeException {

	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -3986258413506587906L;
	
	private String errorMessage;
	private HttpStatus httpStatus;
	
	public GameException() {
		super();
	}
	
	public GameException(String errorMessage, HttpStatus httpStatus) {
		super();
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public GameException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
