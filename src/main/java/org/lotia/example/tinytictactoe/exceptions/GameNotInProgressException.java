package org.lotia.example.tinytictactoe.exceptions;

public class GameNotInProgressException extends RuntimeException {

	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -432143606324108820L;

	public GameNotInProgressException(String gameId) {
		super(gameId + " not found");
	}
}
