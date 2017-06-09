package org.lotia.example.tinytictactoe.exceptions;

public class IllegalMoveException extends RuntimeException {

	// Move to a square that is already covered
	// Move out of bounds
	
	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -3742524374937985262L;

	public IllegalMoveException(String message) {
		super(message);
	}
}
