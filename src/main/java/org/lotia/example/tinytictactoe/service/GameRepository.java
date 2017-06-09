package org.lotia.example.tinytictactoe.service;

import java.util.Set;

import org.lotia.example.tinytictactoe.model.Game;

public interface GameRepository {

	public boolean gameExists(String gameId);
	public Game findGameById(String gameId);
	public void saveGameById(String gameId, Game game);
	public void deleteGameById(String gameId);
	public Set<String> getAllGameIds();
}
