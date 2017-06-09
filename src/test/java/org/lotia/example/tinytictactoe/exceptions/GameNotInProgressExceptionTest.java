package org.lotia.example.tinytictactoe.exceptions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class GameNotInProgressExceptionTest {

	private static final String gameId = "1";
	
	@Test
	public void testGameNotInProgressException_constructor() {
		final GameNotInProgressException e = new GameNotInProgressException(gameId);
		assertThat(e.getMessage(), equalTo("gameId=1 not in progress"));
	}
}
