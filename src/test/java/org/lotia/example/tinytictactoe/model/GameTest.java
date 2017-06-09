package org.lotia.example.tinytictactoe.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.IllegalMoveException;


public class GameTest {

	@Test
	public void testEmptyConstructor() throws Exception {
		Game game = new Game();
		assertNotNull(game);
		Character[][] board = game.getBoard();
		assertThat(board.length, equalTo(GameConstants.DEFAULT_BOARD_SIZE));
		assertTrue(boardIsEmpty(board));
	}
	
	
	@Test
	public void testConstructorWithId() throws Exception {
		final String gameId = "foo";
		Game game = new Game(gameId);
		assertNotNull(game);
		assertThat(game.getId(), equalTo(gameId));
		assertThat(game.getGameStatus(), equalTo(GameStatus.INPROGRESS));
		assertTrue(boardIsEmpty(game.getBoard()));
	}
	
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetBoardMarker() throws Exception {
		Game game = new Game();
		game.getBoardMarker(GameConstants.DEFAULT_BOARD_SIZE, 0);
	}
	
	@Test(expected = IllegalMoveException.class)
	public void testSetBoardMarker_invalidRow() throws Exception {
		Game game = new Game();
		game.setBoardMarker(GameConstants.DEFAULT_BOARD_SIZE, 0, GameConstants.PLAYER_MARKER);
	}
	
	@Test(expected = IllegalMoveException.class)
	public void testSetBoardMarker_invalidColumn() throws Exception {
		Game game = new Game();
		game.setBoardMarker(0, GameConstants.DEFAULT_BOARD_SIZE, GameConstants.PLAYER_MARKER);
	}
	

	@Test(expected = IllegalMoveException.class)
	public void testSetBoardMarker_invalidMarker() throws Exception {
		Game game = new Game();
		game.setBoardMarker(0, 0, GameConstants.EMPTY_SPACE_MARKER);
	}
	
	private boolean boardIsEmpty(Character[][] board) {
		for (int i=0; i < board.length; i++) {
			for (int j=0; j < board.length; j++) {
				if (board[i][j] != GameConstants.EMPTY_SPACE_MARKER) {
					return false;
				}
			}
		}
		return true;
	}
}
