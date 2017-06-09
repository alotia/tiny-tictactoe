package org.lotia.example.tinytictactoe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Game not found")
public class GameNotFoundException extends Exception {

	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = 1435010653069899548L;

	public GameNotFoundException(String gameId) {
		super("gameId=" + gameId + " not found");
	}
}
