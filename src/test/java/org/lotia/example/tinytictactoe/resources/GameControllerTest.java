package org.lotia.example.tinytictactoe.resources;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameException;
import org.lotia.example.tinytictactoe.exceptions.GameNotFoundException;
import org.lotia.example.tinytictactoe.exceptions.GameNotInProgressException;
import org.lotia.example.tinytictactoe.exceptions.IllegalMoveException;
import org.lotia.example.tinytictactoe.model.Game;
import org.lotia.example.tinytictactoe.model.GameStatus;
import org.lotia.example.tinytictactoe.service.GameService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {


    @InjectMocks
    GameController gameController;
    @Mock
    GameService gameService;
    
    @Test
    public void testGetGame() throws Exception {
    		String gameId = "1";
    		Game expectedGame = Mockito.mock(Game.class);
    		
    		when(gameService.getGame(gameId)).thenReturn(expectedGame);
    		ResponseEntity<Game> actualGame = gameController.getGame(gameId);
    		
    		assertThat(actualGame.getStatusCode(), equalTo(HttpStatus.OK));
    		assertThat(actualGame.getBody(), equalTo(expectedGame));
    }
    
    
    @SuppressWarnings("unchecked")
	@Test(expected = GameException.class)
    public void testGetGame_GameNotFoundException() throws Exception {
    		String gameId = "invalid";
    		
    		when(gameService.getGame(gameId)).thenThrow(GameNotFoundException.class);
    		gameController.getGame(gameId);
    }
	
    @Test
    public void testGetAllGames() throws Exception {
    		Game game1 = Mockito.mock(Game.class);
    		Game game2 = Mockito.mock(Game.class);
    		List<Game> games = new ArrayList<>();
    		games.add(game1);
    		games.add(game2);
    		
    		when(gameService.getAllGames()).thenReturn(games);
    		ResponseEntity<List<Game>> actualGames = gameController.getAllGames();
    		
    		assertThat(actualGames.getStatusCode(), equalTo(HttpStatus.OK));  
    		assertEquals(game1, actualGames.getBody().get(0));
    		assertEquals(game2, actualGames.getBody().get(1));
    }
    
    @Test(expected = GameException.class)
    public void testCreateNewGame_nonIntegerBoardSize() throws Exception {
    		gameController.createNewGame("playFirst", "invalidInteger");
    }
    
    @Test
    public void testCreateNewGame() {
    		int boardSize = GameConstants.DEFAULT_BOARD_SIZE;
    		String boardSizeAsString = "3";
    		String gameId = "foo";
		Game expectedGame = new Game(gameId);
		
		when(gameService.createGame(boardSize)).thenReturn(expectedGame);
		ResponseEntity<Game> actualGame = gameController.createNewGame("dummy", boardSizeAsString);
		
		assertThat(actualGame.getBody().getId(), equalTo(gameId));
		assertThat(actualGame.getBody().getGameStatus(), equalTo(GameStatus.INPROGRESS));
		assertThat(actualGame.getBody().getBoard().length, equalTo(boardSize));
		assertThat(actualGame.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(actualGame.getBody(), equalTo(expectedGame));
    }   
    
    @Test(expected = GameException.class)
    public void testPlayMove_GameIdConflictException() throws Exception {
    		String gameId = "foo";
    		Game game = new Game(gameId);
    		
    		gameController.playMove("bar", game);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected = GameException.class)
    public void testPlayMove_GameNotFoundException() throws Exception {
    		String gameId = "foo";
    		Game game = new Game(gameId);
    		
    		when(gameService.getGame(gameId)).thenThrow(GameNotFoundException.class);
		gameController.playMove(gameId, game);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected = GameException.class)
    public void testPlayMove_IllegalMoveException() throws Exception {
    		String gameId = "foo";
    		Game game = new Game(gameId);
    		
    		when(gameService.registerMove(gameId, game)).thenThrow(IllegalMoveException.class);
		gameController.playMove(gameId, game);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected = GameException.class)
    public void testPlayMove_GameNotInProgressException() throws Exception {
    		String gameId = "foo";
    		Game game = new Game(gameId);
    		
    		when(gameService.registerMove(gameId, game)).thenThrow(GameNotInProgressException.class);
		gameController.playMove(gameId, game);
    }
    
    // TODO: Add more tests for the "delete" APIs
    
}
