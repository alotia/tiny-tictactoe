package org.lotia.example.tinytictactoe.service.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameIdConflictException;
import org.lotia.example.tinytictactoe.model.Game;


public class GameValidationUtilsTest {
	
	@Test
	public void testIdInGameMatchesGameId_idsMatch() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		try {
			GameValidationUtils.idInGameMatchesGameId(game, gameId);
		} catch (Exception e) {
			fail("Expected Ids to match!");
		}
	}
	
	@Test(expected = GameIdConflictException.class)
	public void testIdInGameMatchesGameId_idsMismatch() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		GameValidationUtils.idInGameMatchesGameId(game, "bar");
	}
	
	@Test
	public void testIsSquareAvailable_available() throws Exception {
		final String gameId = "foo";
		final Game newGameBoard = new Game(gameId);
		int newLocation[] = {1, 1};
		newGameBoard.setLocation(newLocation);
		
		// existingGameBoard has no markers, so newLocation should be available
		assertTrue(GameValidationUtils.isSquareAvailable(newGameBoard));
	}
	
	@Test
	public void testIsSquareAvailable_notAvailable() throws Exception {
		final String gameId = "foo";
		int row = 1;
		int column = 1;
		final Game newGameBoard = new Game(gameId);
		int newLocation[] = {row, column};
		// mark the existing location with a marker
		newGameBoard.setBoardMarker(row, column, GameConstants.PLAYER_MARKER);
		newGameBoard.setLocation(newLocation);
		assertFalse(GameValidationUtils.isSquareAvailable(newGameBoard));
	}
	
	
	// TODO: Tests for GameValidationUtils.determineGameStatus
}
