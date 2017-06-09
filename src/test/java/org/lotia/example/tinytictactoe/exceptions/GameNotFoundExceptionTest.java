package org.lotia.example.tinytictactoe.exceptions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class GameNotFoundExceptionTest {
	
		private static final String gameId = "1";

		@Test
		public void testGameIdConflictException_constructor() {
			final GameNotFoundException e = new GameNotFoundException(gameId);
			assertThat(e.getMessage(), equalTo("gameId=1 not found"));
		}

}
