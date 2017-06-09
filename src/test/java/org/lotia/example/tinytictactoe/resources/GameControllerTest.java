package org.lotia.example.tinytictactoe.resources;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lotia.example.tinytictactoe.model.Game;
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
    
    // TODO: Add more tests for other APIs
    
   
}
