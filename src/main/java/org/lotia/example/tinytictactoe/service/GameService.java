package org.lotia.example.tinytictactoe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameNotFoundException;
import org.lotia.example.tinytictactoe.exceptions.GameNotInProgressException;
import org.lotia.example.tinytictactoe.exceptions.IllegalMoveException;
import org.lotia.example.tinytictactoe.model.Game;
import org.lotia.example.tinytictactoe.model.GameStatus;
import org.lotia.example.tinytictactoe.service.util.GameValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameService {

	private static AtomicLong gameIdCounter = new AtomicLong();
	
	@Autowired
	private GameRepository gameRepository;
	
	
	
	public Game createGame(int boardSize) {
		String gameId = String.valueOf(gameIdCounter.incrementAndGet());
		Game game = new Game(gameId);
		gameRepository.saveGameById(gameId, game);
		return game;
	}
	
	public Game getGame(String gameId) throws GameNotFoundException {
		if (! gameRepository.gameExists(gameId)) {
			throw new GameNotFoundException(gameId);
		}
		return gameRepository.findGameById(gameId);
	}
	
	public List<Game> getAllGames() {
		List<Game> allGames = new ArrayList<>();
		
		Set<String> gameIds = gameRepository.getAllGameIds();
		for (String gameId : gameIds) {
			allGames.add(gameRepository.findGameById(gameId));
		}
		
		return allGames;
	}
	
	public Game registerMove(String gameId, Game game) 
			throws GameNotFoundException,  IllegalMoveException, GameNotInProgressException { 
		
		// make sure the game is valid and the move is valid
		if (! gameRepository.gameExists(gameId)) {
			throw new GameNotFoundException(gameId);
		}
		
		Game existingGame = gameRepository.findGameById(gameId);
		
		// Check if the game is in progress
		if (existingGame.getGameStatus() != GameStatus.INPROGRESS) {
			throw new GameNotInProgressException(gameId);
		}
		
		// registerMove will throw an Illegal move exception for invalid moves
		// Otherwise it will add the move and return the status of the game
		GameStatus gameStatus = playOneTurn(gameId, existingGame, game);
		game.setGameStatus(gameStatus);
		
		// Save the updated game and return it
		gameRepository.saveGameById(gameId, game);
		
		return game;
	}
	
	public void deleteAllGames() {
		Set<String> gameIds = gameRepository.getAllGameIds();
		for (String gameId : gameIds) {
			gameRepository.deleteGameById(gameId);
		}
	}
	
	public void deleteGame(String gameId) throws GameNotFoundException {
		
		// make sure the game is valid and the move is valid
		if (! gameRepository.gameExists(gameId)) {
			throw new GameNotFoundException(gameId);
		}
		gameRepository.deleteGameById(gameId);
	}
	
	private GameStatus playOneTurn(String gameId, Game previousGameState, Game newGameState) 
			throws IllegalMoveException {
		
		// Add the player's (service user) move and check if the game is a win player or a draw
		GameStatus gameStatusAfterMove;
		gameStatusAfterMove = registerPlayerMove(gameId, previousGameState, newGameState);
		
		if (gameStatusAfterMove != GameStatus.DRAW || gameStatusAfterMove != GameStatus.PLAYERWON) {
			
			// If the game is not over after player's move, then add a move by the service
			// Add service's move and check if the game is a win (by service) or a draw
			gameStatusAfterMove = registerServiceMove(previousGameState, newGameState);
		}
		
		return gameStatusAfterMove;
	}


	private GameStatus registerPlayerMove(String gameId, Game previousGameState, Game newGameState) 
			throws IllegalMoveException {
	
		// Check if the square is not already occupied
		if (! GameValidationUtils.isSquareAvailable(previousGameState, newGameState)) {
			int[] location = newGameState.getLocation();
			throw new IllegalMoveException("gameId=" + gameId + " row=" + location[0] + " column=" + location[1]);
		}
		
		// record the move by player and check if it is a win or draw
		int[] newMoveLocation = newGameState.getLocation();
		int newRow = newMoveLocation[0];
		int newColumn = newMoveLocation[1];
		newGameState.setBoardMarker(newRow, newColumn, GameConstants.PLAYER_MARKER);
		
		GameStatus newGameStatus = GameValidationUtils.determineGameStatus(newGameState);
		newGameState.setGameStatus(newGameStatus);
		
		return newGameStatus;
	}
	
	
	/*
	 * TODO: Refactor this to an interface where the service can choose
	 * between a dumb or smart move
	 */
	private GameStatus registerServiceMove(Game previousGameState, Game newGameState) {
		
		// Make a dumb move by finding the first open square		
		int row = 0;
		int column = 0;
		int boardSize = previousGameState.getBoard().length;
		boolean emptySquareFound = false;
		for (row=0; row < boardSize; row++) {
			for (column=0; column < boardSize; column++) {
				if (previousGameState.getBoardMarker(row, column) == GameConstants.EMPTY_SPACE_MARKER) {
					emptySquareFound = true;
					break;
				}
			}
			if (emptySquareFound) {
				break;
			}
		}
		
		newGameState.setBoardMarker(row, column, GameConstants.SERVICE_MARKER);		
		GameStatus newGameStatus = GameValidationUtils.determineGameStatus(newGameState);
		
		return newGameStatus;
	}	
}

