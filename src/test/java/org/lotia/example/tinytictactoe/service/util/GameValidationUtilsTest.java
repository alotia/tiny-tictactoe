package org.lotia.example.tinytictactoe.service.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameIdConflictException;
import org.lotia.example.tinytictactoe.model.Game;
import org.lotia.example.tinytictactoe.model.GameStatus;


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
	
	@Test
	public void testGameInProgress() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"XO ", "O X", "X  "};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.INPROGRESS, GameValidationUtils.determineGameStatus(game));
	}
	
	@Test
	public void testPlayerWon_rowComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"XOX", "XXX", "OXO"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.PLAYERWON, GameValidationUtils.determineGameStatus(game));
	}
	
	@Test
	public void testPlayerWon_columnComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"OXO", "XXO", "OXX"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.PLAYERWON, GameValidationUtils.determineGameStatus(game));
	}
	
	@Test
	public void testPlayerWon_diagonalComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"XOO", "OXO", "OXX"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.PLAYERWON, GameValidationUtils.determineGameStatus(game));
	}
	
	@Test
	public void testPlayerWon_antiDiagonalComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"OOX", "OXO", "XOO"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.PLAYERWON, GameValidationUtils.determineGameStatus(game));
	}
	
	@Test
	public void testServicePlayerWon_rowComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"OXO", "XXO", "XOO"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.SERVICEWON, GameValidationUtils.determineGameStatus(game));
	}
	
	
	@Test
	public void testServicePlayerWon_columnComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"XOX", "OOX", "XOO"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.SERVICEWON, GameValidationUtils.determineGameStatus(game));
	}
	
	@Test
	public void testServicePlayerWon_diagonalComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"OXX", "XOX", "XOO"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.SERVICEWON, GameValidationUtils.determineGameStatus(game));
	}
	
	
	@Test
	public void testServicePlayerWon_antiDiagonalComplete() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"XXO", "XOX", "OXX"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.SERVICEWON, GameValidationUtils.determineGameStatus(game));
	}
	
	
	@Test
	public void testDraw() throws Exception {
		final String gameId = "foo";
		final Game game = new Game(gameId);
		String[] rows = {"XOX", "OXO", "OXO"};
		final Character[][] board = create3x3Board(rows);
		game.setBoard(board);
	
		assertEquals(GameStatus.DRAW, GameValidationUtils.determineGameStatus(game));
	}
	
	
	// utility method to create a board in a given configuration
	private Character[][] create3x3Board(String[] rows) {
		Character[][] board = new Character[GameConstants.DEFAULT_BOARD_SIZE][GameConstants.DEFAULT_BOARD_SIZE];
		for (int i=0; i < GameConstants.DEFAULT_BOARD_SIZE; i++) {
			for (int j=0; j < GameConstants.DEFAULT_BOARD_SIZE; j++) {
				board[i][j] = rows[i].charAt(j);
			}
		}
		return board;
	}
}
