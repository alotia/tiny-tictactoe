package org.lotia.example.tinytictactoe.exceptions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class IllegalMoveExceptionTest {
	
	private static final String message = "Some message";

	@Test
	public void testIllegalMoveException_constructor() {
		final IllegalMoveException e = new IllegalMoveException(message);
		assertThat(e.getMessage(), equalTo(message));
	}
}
