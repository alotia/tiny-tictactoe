package org.lotia.example.tinytictactoe.resources;

import java.util.List;

import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameIdConflictException;
import org.lotia.example.tinytictactoe.exceptions.GameNotFoundException;
import org.lotia.example.tinytictactoe.model.Game;
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
@Api(value = "GamesControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {
	
	private final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	@Autowired
	private GameService gameService;
	
	
	// GET all the games
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation("Returns all the games created")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Game.class)})
	public ResponseEntity<List<Game>> getAllGames() {
		
		List<Game> games = gameService.getAllGames();
		return new ResponseEntity<List<Game>>(games, HttpStatus.OK);
	}
	
	
	// CREATE a new game
	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation("Create a new game")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Game.class)})
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
	public ResponseEntity<?> deleteAllGames() {
		logger.debug("GameController:deleteAllGames");
		
		gameService.deleteAllGames();
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	// Get the state of a current game
	@RequestMapping(value="/{gameId}", method = RequestMethod.GET)
	public ResponseEntity<Game> getGame(
			@PathVariable("gameId") String gameId) {
		logger.debug("GameController:getGame gameId={}", gameId);
		
		Game game;
		try {
			game = gameService.getGame(gameId);
		} catch (GameNotFoundException e) {
			logger.warn("Couldn't find gameId=" + gameId);
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Game>(game, HttpStatus.OK);
	}
	
	
	// DELETE a specific game
	@RequestMapping(value="/{gameId}", method = RequestMethod.DELETE)
	public ResponseEntity<Game> deleteGame(
			@PathVariable("gameId") String gameId) {
		logger.debug("GameController:deleteGame gameId={}", gameId);
		
		try {
			gameService.getGame(gameId);
		} catch (GameNotFoundException e) {
			logger.warn("Couldn't find gameId=" + gameId);
			return new ResponseEntity<Game>(HttpStatus.NOT_FOUND);
		}		
		
		gameService.deleteGame(gameId);
		return new ResponseEntity<Game>(HttpStatus.OK);
	}
	

	// ADD a move by user
	@RequestMapping(value="/{gameId}", method = RequestMethod.POST)
	public ResponseEntity<Game> playMove(
			@PathVariable("gameId") String gameId,
			@RequestBody Game game) {
		logger.debug("GameController:playMove gameId={}, game={}", gameId, game);
		
		try {
			GameValidationUtils.idInGameMatchesIdGameId(game, gameId);
		} catch (GameIdConflictException e) {
			logger.warn("id=" + game.getId() + " in game doesn't match gameId=" + gameId);
		}
		
		Game newGame = gameService.registerMove(gameId, game);
		return new ResponseEntity<Game>(newGame, HttpStatus.OK);
	}
}
