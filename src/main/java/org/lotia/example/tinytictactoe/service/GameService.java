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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * This is the main service class called by the gameController to handle all the game-related requests.
 */
@Component
public class GameService {

	private final Logger logger = LoggerFactory.getLogger(GameService.class);
	
	private static AtomicLong gameIdCounter = new AtomicLong();
	
	@Autowired
	private GameRepository gameRepository;
	
	
	/**
	 * Create a new game board. The id is a static counter and incremented for every request.
	 * The Game constructor will also initialize the game board with empty squares and will
	 * mark the game as being INPROGRESS.
	 * 
	 * @param boardSize the size N of the NxN board. Boards are always square.
	 * @return a new {@link Game} object
	 */
	public Game createGame(int boardSize) {
		final String gameId = String.valueOf(gameIdCounter.incrementAndGet());
		Game game = new Game(gameId);
		gameRepository.saveGameById(gameId, game);
		return game;
	}
	
	/**
	 * Return a game for the given gameId. If the gameId doesn't exist in the repository, the
	 * method throws a GameNotFoundException
	 * 
	 * @param gameId The game to fetch
	 * @return a {@link Game} object
	 * @throws GameNotFoundException if the game does not exist in the repository
	 */
	public Game getGame(String gameId) throws GameNotFoundException {
		if (! gameRepository.gameExists(gameId)) {
			throw new GameNotFoundException(gameId);
		}
		return gameRepository.findGameById(gameId);
	}
	
	/**
	 * Get all the games from the repository. If the repository is empty, return an empty list.
	 * 
	 * @return a {@link List} of {@link Game} objects
	 */
	public List<Game> getAllGames() {
		List<Game> allGames = new ArrayList<>();
		
		final Set<String> gameIds = gameRepository.getAllGameIds();
		logger.debug("GameService:getAllGames Game repositry has {} games registered", gameIds.size());
		
		for (String gameId : gameIds) {
			allGames.add(gameRepository.findGameById(gameId));
		}
		
		return allGames;
	}
	
	
	/**
	 * Given a game board sent by player, check that the game exists (just for validation),
	 * and that the game is not already over. Then play the move specified. If the move is invalid,
	 * the helper methods will throw an exception.
	 * 
	 * @param gameId The id of the game in which to move
	 * @param game The game board as sent by the player
	 * @return An updated {@link Game} after recording the player's move and, if available, the service's move
	 * @throws GameNotFoundException If the gameId is not found in the repository
	 * @throws IllegalMoveException If the location specified by the user is out of board boundaries, or the square is already marked.
	 * @throws GameNotInProgressException If the game specified by the gameId in not INPROGRESS
	 */
	public Game registerMove(final String gameId, Game game) 
			throws GameNotFoundException,  IllegalMoveException, GameNotInProgressException { 
		
		// make sure the game is valid
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
		GameStatus gameStatus = playOneTurn(gameId, game);
		game.setGameStatus(gameStatus);
		
		// Save the updated game and return it
		gameRepository.saveGameById(gameId, game);
		
		return game;
	}
	
	/**
	 * Delete all the games in the repository; essentially resetting the repository.
	 */
	public void deleteAllGames() {
		final Set<String> gameIds = gameRepository.getAllGameIds();
		logger.debug("GameService:deleteAllGames Deleting {} games from repository", gameIds.size());
		
		for (String gameId : gameIds) {
			gameRepository.deleteGameById(gameId);
		}
	}
	
	/**
	 * Delete a game specified by the gameId from the repository. If the game isn't found, it throws a GameNotFoundException
	 * @param gameId The id of the game to delete
	 * @throws GameNotFoundException If the game is not in the repository
	 */
	public void deleteGame(final String gameId) throws GameNotFoundException {
		
		// make sure the game is valid and the move is valid
		if (! gameRepository.gameExists(gameId)) {
			throw new GameNotFoundException(gameId);
		}
		gameRepository.deleteGameById(gameId);
	}
	
	/*
	 * This method "one turn". In this case a turn is play by the user, followed by a play by the service
	 * if, after the user's play, the user hasn't won and the game isn't a draw (i.e. board is not completely filled).
	 * If the service makes a move, the board is again checked to see if the service has won, or the game
	 * is a draw.
	 */
	private GameStatus playOneTurn(final String gameId, Game newGameState) 
			throws IllegalMoveException {
		
		// Add the player's (service user) move and check if the game is a win player or a draw
		GameStatus gameStatusAfterMove;
		gameStatusAfterMove = registerPlayerMove(gameId, newGameState);
		
		// If the game is not over after player's move, then add a move by the service
		// Add service's move and check if the game is a win (by service) or a draw
		if (gameStatusAfterMove != GameStatus.DRAW && gameStatusAfterMove != GameStatus.PLAYERWON) {
			logger.debug("GameService:playOneTurn Proceeding with service move after recording player's move");
			gameStatusAfterMove = registerServiceMove(newGameState);
		}
		
		return gameStatusAfterMove;
	}


	/*
	 * This makes the player's move after validating it, and set the status of the game accordingly.
	 */
	private GameStatus registerPlayerMove(final String gameId, Game newGameState) 
			throws IllegalMoveException {
	
		// Check if the square is not already occupied
		if (! GameValidationUtils.isSquareAvailable(newGameState)) {
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
	 * between a dumb or smart move. In this case, the service simply plays the first available square
	 * that it sees scanning from left to right, top to bottom.
	 */
	private GameStatus registerServiceMove(Game newGameState) {
		
		// Make a dumb move by finding the first open square		
		int row = 0;
		int column = 0;
		int boardSize = newGameState.getBoard().length;
		boolean emptySquareFound = false;
		for (row=0; row < boardSize; row++) {
			for (column=0; column < boardSize; column++) {
				if (newGameState.getBoardMarker(row, column) == GameConstants.EMPTY_SPACE_MARKER) {
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

