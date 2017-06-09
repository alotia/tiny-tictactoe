package org.lotia.example.tinytictactoe.model;

public enum GameStatus {
	INPROGRESS,  // A game being played. A new game starts in this state
	DRAW,		 // The game board is full, and no player has won
	PLAYERWON,   // The human player ('X') has won
	SERVICEWON   // The service ('O') has won
}
