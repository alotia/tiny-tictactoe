package org.lotia.example.tinytictactoe.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.lotia.example.tinytictactoe.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameRepositoryImpl implements GameRepository {

	private static Map<String, Game> games = new ConcurrentHashMap<>();
	
	@Override
	public boolean gameExists(String gameId) {
		if (games.containsKey(gameId)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Game findGameById(String gameId) {
		return games.get(gameId);
	}

	@Override
	public void saveGameById(String gameId, Game game) {
		games.put(gameId, game);
	}
	
	@Override
	public Set<String> getAllGameIds() {
		return games.keySet();
	}

	@Override
	public void deleteGameById(String gameId) {
		games.remove(gameId);
	}

	
}
