package org.lotia.example.tinytictactoe.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lotia.example.tinytictactoe.GameConstants;
import org.lotia.example.tinytictactoe.exceptions.GameNotFoundException;
import org.lotia.example.tinytictactoe.model.Game;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
	
	@Mock
	private GameRepository gameRepository;
	
	@InjectMocks
	private GameService gameService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCreateGame() throws Exception {
		String gameId = "foo";
		Game game = new Game(gameId);
		doNothing().when(gameRepository).saveGameById(gameId, game);
		Game createdGame = gameService.createGame(GameConstants.DEFAULT_BOARD_SIZE);
		System.out.println("game=" + createdGame);
	}
	
	@Test(expected = GameNotFoundException.class)
	public void testGetGame_gameNotFoundException() throws Exception {
		String gameId = "foo";
		when(gameRepository.gameExists(gameId)).thenReturn(false);
		gameService.getGame(gameId);
	}
	
	@Test
	public void testGetGame_gameFound() throws Exception {
		String gameId = "foo";
		Game game = new Game(gameId);
		when(gameRepository.gameExists(gameId)).thenReturn(true);
		when(gameRepository.findGameById(gameId)).thenReturn(game);
		
		Game actualGame = gameService.getGame(gameId);
		assertEquals(actualGame, game);
	}
	
	@Test
	public void testGetAllGames() throws Exception {
		String gameId1 = "foo1";
		Game game1 = new Game(gameId1);
		String gameId2 = "foo2";
		Game game2 = new Game(gameId2);
		Set<String> gameIds = new HashSet<>();
		gameIds.add(gameId1);
		gameIds.add(gameId2);
		
		when(gameRepository.getAllGameIds()).thenReturn(gameIds);
		when(gameRepository.findGameById(gameId1)).thenReturn(game1);
		when(gameRepository.findGameById(gameId2)).thenReturn(game2);
		
		List<Game> actualGames = gameService.getAllGames();
		boolean game1Found = false;
		boolean game2Found = false;
		for (Game game : actualGames) {
			if (game.equals(game1)) {
				game1Found = true;
			} else if (game.equals(game2)) {
				game2Found = true;
			}
		}
		assertTrue(game1Found && game2Found);
	}
	
	@Test(expected = GameNotFoundException.class)
	public void testDeleteGame_gameNotFoundException() throws Exception {
		String gameId = "foo";
		when(gameRepository.gameExists(gameId)).thenReturn(false);
		gameService.deleteGame(gameId);
	}
	
	@Test
	public void testDeleteGame_gameFound() throws Exception {
		String gameId = "foo";
		when(gameRepository.gameExists(gameId)).thenReturn(true);
		doNothing().when(gameRepository).deleteGameById(gameId);
		gameService.deleteGame(gameId);
	}
	
	@Test
	public void testDeleteAllGames() throws Exception {
		String gameId1 = "foo1";
		String gameId2 = "foo2";
		Set<String> gameIds = new HashSet<>();
		gameIds.add(gameId1);
		gameIds.add(gameId2);
		
		when(gameRepository.getAllGameIds()).thenReturn(gameIds);
		doNothing().when(gameRepository).deleteGameById(gameId1);
		doNothing().when(gameRepository).deleteGameById(gameId2);
		gameService.deleteAllGames();
		verify(gameRepository, times(1)).deleteGameById(gameId1);
		verify(gameRepository, times(1)).deleteGameById(gameId2);
	}
	
}
