package org.lotia.example.tinytictactoe.service;

import java.util.Set;

import org.lotia.example.tinytictactoe.model.Game;

/**
 * The interface for the games repository
 */
public interface GameRepository {

	/**
	 * Check if a game exists for the specified gameId.
	 * 
	 * @param gameId Lookup id for the game
	 * @return true if gameId exists in the repository, false otherwise
	 */
	public boolean gameExists(String gameId);
	
	
	/**
	 * Given a gameId, return the game specified by that gameId
	 * 
	 * @param gameId The id of the game to get
	 * @return The {@link Game} if the game exists in repository, null otherwise
	 */
	public Game findGameById(String gameId);
	
	
	/**
	 * Save (or update) a {@link Game} specified by the gameId
	 * 
	 * @param gameId The id of the game to save or update
	 * @param game The {@link Game} object
	 */
	public void saveGameById(String gameId, Game game);
	
	
	/**
	 * Delete a game for the given id
	 * 
	 * @param gameId The {@link Game} to delete specified by the gameId
	 */
	public void deleteGameById(String gameId);
	
	/**
	 * Get a list of all the gameIds in the repository
	 * 
	 * @return A {@link Set} of gameIds
	 */
	public Set<String> getAllGameIds();
}
