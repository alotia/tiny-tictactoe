package org.lotia.example.tinytictactoe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Id in game does not match gameId in param")
public class GameIdConflictException extends Exception {

	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -7792056165746988759L;
	
	public GameIdConflictException(String gameId, String id) {
		super("gameId=" + gameId + " does not match id=" + id);
	}
}
