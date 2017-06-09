package org.lotia.example.tinytictactoe.exceptions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class GameIdConflictExceptionTest {

	private static final String gameId = "1";
	private static final String id = "2";

	@Test
	public void testGameIdConflicException_constructor() {
		final GameIdConflictException e = new GameIdConflictException(gameId, id);
		assertThat(e.getMessage(), equalTo("gameId=1 does not match id=2"));
	}
}
