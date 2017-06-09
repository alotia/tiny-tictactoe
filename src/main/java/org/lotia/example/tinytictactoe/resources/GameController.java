package org.lotia.example.tinytictactoe.resources;

import java.util.List;

import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameIdConflictException;
import org.lotia.example.tinytictactoe.exceptions.GameNotFoundException;
import org.lotia.example.tinytictactoe.exceptions.GameNotInProgressException;
import org.lotia.example.tinytictactoe.exceptions.IllegalMoveException;
import org.lotia.example.tinytictactoe.model.Game;
import org.lotia.example.tinytictactoe.model.GameStatus;
import org.lotia.example.tinytictactoe.service.GameService;
import org.lotia.example.tinytictactoe.service.util.GameValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/games")
@Api(value = "GamesController API", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {
	
	private final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	@Autowired
	private GameService gameService;
	
	
	// GET all the games
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation("Return all the games created.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Game.class)})
	public ResponseEntity<List<Game>> getAllGames() {
		
		List<Game> games = gameService.getAllGames();
		return new ResponseEntity<List<Game>>(games, HttpStatus.OK);
	}
	
	
	// CREATE a new game
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(value = "Create a new game.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = Game.class),
			@ApiResponse(code = 400, message = "Bad Request")
	})
	public ResponseEntity<Game> createNewGame(
		@RequestParam(value="playFirst", defaultValue="false") String playFirst,
		@RequestParam(value="boardSize", defaultValue="3") String boardSize) {
		
		logger.debug("GameController:createNewGame playFirst={}, boardSize={}", playFirst, boardSize);
		
		int size = GameConstants.DEFAULT_BOARD_SIZE;
		try {
			size = Integer.parseInt(boardSize);
			
		} catch (NumberFormatException e) {
			logger.warn("Invalid boardSize given boardSize=" + boardSize);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Game game = gameService.createGame(size);
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	
	
	// DELETE all the games
	@RequestMapping(method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete all games.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Object.class)})
	public ResponseEntity<?> deleteAllGames() {
		logger.debug("GameController:deleteAllGames");
		
		gameService.deleteAllGames();
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	// GET the state of a current game
	@RequestMapping(value="/{gameId}", method = RequestMethod.GET)
	@ApiOperation(
			value = "Get the state of a game.",
			notes ="Returns the entire game board with in it's current state.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Game.class),
		@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<Game> getGame(
			@PathVariable("gameId") String gameId) {
		logger.debug("GameController:getGame gameId={}", gameId);
		
		Game game;
		try {
			game = gameService.getGame(gameId);
		} catch (GameNotFoundException e) {
			logger.warn("Couldn't find gameId=" + gameId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	

	// ADD a move by user
	@RequestMapping(value="/{gameId}", method = RequestMethod.POST)
	@ApiOperation(
			value = "Play a move specified by user (and service)",
			notes = "Records a move specified by a user. <br/>"
				  + "If after the user's move the game is won by user or is a draw (i.e. "
				  + "the board is not full) the service will register its move.<br/>"
				  + "The state of the board will be checked after the move to see if the service has won, or if it draw.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Game.class),
		@ApiResponse(code = 404, message = "Not Found"),
		@ApiResponse(code = 400, message = "Conflict")
	})
	public ResponseEntity<Game> playMove(
			@PathVariable("gameId") String gameId,
			@RequestBody Game game) {
		logger.debug("GameController:playMove gameId={}, game={}", gameId, game);
		
		try {
			gameService.getGame(gameId);
			GameValidationUtils.idInGameMatchesGameId(game, gameId);
			Game newGame = gameService.registerMove(gameId, game);
			return new ResponseEntity<Game>(newGame, HttpStatus.OK);
		} catch (GameNotFoundException e) {
			logger.warn("Couldn't find gameId=" + gameId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (GameIdConflictException e) {
			logger.warn("id=" + game.getId() + " in game doesn't match gameId=" + gameId);
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (IllegalMoveException e) {
			int[] location = game.getLocation();
			logger.warn("Illegal move specified in request. row=" + location[0] + " column=" + location[1]);
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (GameNotInProgressException e) {
			logger.warn("Game gameId=" + gameId + " does not have " + GameStatus.INPROGRESS + " status");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	
	// DELETE a specific game
	@RequestMapping(value="/{gameId}", method = RequestMethod.DELETE)
	@ApiOperation("Delete a game.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "Not Found")
	})
	public ResponseEntity<?> deleteGame(
			@PathVariable("gameId") String gameId) {
		logger.debug("GameController:deleteGame gameId={}", gameId);
		
		try {
			gameService.getGame(gameId);
			gameService.deleteGame(gameId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (GameNotFoundException e) {
			logger.warn("Couldn't find gameId=" + gameId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
