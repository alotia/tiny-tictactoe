package org.lotia.example.tinytictactoe.service.util;

import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameIdConflictException;
import org.lotia.example.tinytictactoe.model.Game;
import org.lotia.example.tinytictactoe.model.GameStatus;

public class GameValidationUtils {


	public static void idInGameMatchesIdGameId(Game game, String gameId) throws GameIdConflictException {
	
		String id = game.getId();
		if (id == null || ! id.equals(gameId)) {
			throw new GameIdConflictException(gameId, "null");
		} 
	}
	
	public static boolean isSquareAvailable(Game previousGameState, Game newGameState) {
		int[] newMoveLocation = newGameState.getLocation();
		int newRow = newMoveLocation[0];
		int newColumn = newMoveLocation[1];
		
		Character[][] board = previousGameState.getBoard();
		if (board[newRow][newColumn] == GameConstants.EMPTY_SPACE_MARKER) {
			return true;
		}
		return false;
	}
	
	
	public static GameStatus determineGameStatus(Game game) {
		
		String playerWinPattern = "XXX";
		String serviceWinPattern = "OOO";
		
		// First check if player or service has won
		Character[][] board = game.getBoard();
		for (int i = 0; i < board.length; i++) {
			if (getBoardRow(board, i).equals(playerWinPattern) 
					|| getBoardColumn(board, i).equals(playerWinPattern)) { // player has a row
				return GameStatus.PLAYERWON;
			} else if (getBoardRow(board, i).equals(serviceWinPattern) 
					|| getBoardColumn(board, i).equals(serviceWinPattern)) { // service has won
				return GameStatus.SERVICEWON;
			}
		}
		
		if (getBoardDiagonal(board).equals(playerWinPattern) || getBoardAntiDiagonal(board).equals(playerWinPattern)) {
			return GameStatus.PLAYERWON;
		} else if (getBoardDiagonal(board).equals(playerWinPattern) || getBoardAntiDiagonal(board).equals(playerWinPattern)) {
			return GameStatus.SERVICEWON;
		}
		
		if (isGameBoardCompleted(board)) {
			return GameStatus.DRAW;
		}
		
		return GameStatus.INPROGRESS;
	}
	
	private static String getBoardRow(Character[][] board, int row) {
		
		StringBuffer sb = new StringBuffer();
		for (int column=0; column < board.length; column++) {
			sb.append(board[row][column]);
		}
		return sb.toString();
	}
	
	private static String getBoardColumn(Character[][] board, int column) {
		
		StringBuffer sb = new StringBuffer();
		for (int row=0; row < board.length; row++) {
			sb.append(board[row][column]);
		}
		return sb.toString();
	}
	
	private static String getBoardDiagonal(Character[][] board) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < board.length; i++) {
			sb.append(board[i][i]);
		}
		return sb.toString();
	}
	
	private static String getBoardAntiDiagonal(Character[][] board) {
		StringBuffer sb = new StringBuffer();
		for (int i=board.length -1; i >= 0; i--) {
			sb.append(board[i][i]);
		}
		return sb.toString();
	}
	
	private static boolean isGameBoardCompleted(Character[][] board) {
		
		// check if all the board squares are taken
		for (int row=0; row < board.length; row++) {
			for (int column=0; column < board.length; column++) {
				if (board[row][column] == GameConstants.EMPTY_SPACE_MARKER) {
					return false;
				}
			}
		}
		return true;
	}
}
