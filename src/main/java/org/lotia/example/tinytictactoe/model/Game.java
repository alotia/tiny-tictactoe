package org.lotia.example.tinytictactoe.model;

import java.util.Arrays;

import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.IllegalMoveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * The main game object. This class is used to map the game schema that is received
 *  (and sent) to the client
 */
public class Game {
	private final Logger logger = LoggerFactory.getLogger(Game.class);
	
	private String id;	
	private int location[] = new int[2];
	private Character[][] board;
	private GameStatus gameStatus;
	
	{
        this.board = new Character[GameConstants.DEFAULT_BOARD_SIZE][GameConstants.DEFAULT_BOARD_SIZE];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = GameConstants.EMPTY_SPACE_MARKER;
            }
        }
    }

	public Game() {
	}

	public Game(String id) {
		this.id = id;
		this.gameStatus = GameStatus.INPROGRESS;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Character[][] getBoard() {
		return board;
	}

	public void setBoard(Character[][] board) {
		this.board = board;
	}

	public int[] getLocation() {
		return location;
	}

	public void setLocation(int[] location) {
		this.location = location;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Character getBoardMarker(int row, int column) throws IndexOutOfBoundsException {
		
		logger.debug("Game:getBoardMarker row={}, column={}", row, column);
		if (row < 0 || row >= board.length || column < 0 || column >= board.length) {
			throw new IndexOutOfBoundsException("Invalid index given for board: row=" + row + " column=" + column);
		}
		return board[row][column];
	}

	// Since a game square cannot be cleared in a move, only allow move for player or service
	public void setBoardMarker(int row, int column, Character marker) throws IllegalMoveException {
		
		logger.debug("Game:setBoardMarker row={}, column={}, marker={}", row, column, marker);
		if (row < 0 || row >= board.length || column < 0 || column >= board.length ||
				(marker != GameConstants.PLAYER_MARKER && marker != GameConstants.SERVICE_MARKER)) {
			throw new IllegalMoveException("Invalid index given for board: row=" + row + " column=" + column);
		}

		board[row][column] = marker;
	}
	
	@Override
	public String toString() {
		return "Game [id=" + id + ", location=" + Arrays.toString(location) + ", board="
				+ printBoard() + ", gameStatus=" + gameStatus + "]";
	}
	
	private String printBoard() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i=0; i < board.length; i++) {
			sb.append("[");
			for (int j=0; j < board.length; j++) {
				sb.append(board[i][j]).append(",");
			}
			sb.append("]");
		}
		sb.append("]");
		return sb.toString();
	}
	
	// we won't be comparing one game to one another, so omitting hashCode and equals methods
}
