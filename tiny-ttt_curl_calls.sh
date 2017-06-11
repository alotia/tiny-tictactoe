#!/bin/sh

# Create a four new games
echo "\n creating first game"
curl -X "POST" "http://localhost:8087/tinytictactoe/games" \
     -H "Content-Type: application/json" 

echo "\n creating second game"
curl -X "POST" "http://localhost:8087/tinytictactoe/games" \
     -H "Content-Type: application/json" 

echo "\n creating third game"
curl -X "POST" "http://localhost:8087/tinytictactoe/games" \
     -H "Content-Type: application/json" 

echo "\n creating fourth game"
curl -X "POST" "http://localhost:8087/tinytictactoe/games" \
     -H "Content-Type: application/json" 


# Get a list of all the games
echo "\n all games in system"
curl "http://localhost:8087/tinytictactoe/games"


# Play a simple game in game #1 where the player wins - player moves: [0,0], [1,0], [2,0]
echo "\n Game 1 Play move [0,0]"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/1" \
     -H "Content-Type: application/json" \
     -d $'{"id":"1","location":[0,0],"board":[[" "," "," "],[" "," "," "],[" "," "," "]],"gameStatus":"INPROGRESS"}'

echo "\n Game 1 Play move [1,0]"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/1" \
     -H "Content-Type: application/json" \
     -d $'{"id":"1","location":[1,0],"board":[["X","O"," "],[" "," "," "],[" "," "," "]],"gameStatus":"INPROGRESS"}'

echo "\n Game 1 Play move [2,0] -- this should end as a win by player"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/1" \
     -H "Content-Type: application/json" \
     -d $'{"id":"1","location":[2,0],"board":[["X","O","O"],["X"," "," "],[" "," "," "]],"gameStatus":"INPROGRESS"}'


# Play a simple game in game #2 where the service wins -- player moves: [1,0], [1,1], [2,0]
echo "\n Game 2 Play move [1,0]"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/2" \
     -H "Content-Type: application/json" \
     -d $'{"id":"2","location":[1,0],"board":[[" "," "," "],[" "," "," "],[" "," "," "]],"gameStatus":"INPROGRESS"}'

echo "\n2 Game 1Play move [1,1]"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/2" \
     -H "Content-Type: application/json" \
     -d $'{"id":"2","location":[1,1],"board":[["O"," "," "],["X"," "," "],[" "," "," "]],"gameStatus":"INPROGRESS"}'

echo "\n Game 2 Play move [2,0] -- this should end as a win by service"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/2" \
     -H "Content-Type: application/json" \
     -d $'{"id":"2","location":[2,0],"board":[["O","O"," "],["X","X"," "],[" "," "," "]],"gameStatus":"INPROGRESS"}'


# Start with a partially completed game board and show that the game ends in a draw
echo "\n Game 3 Play move [2,2] -- this should end in a DRAW"
curl -X "POST" "http://localhost:8087/tinytictactoe/games/3" \
     -H "Content-Type: application/json" \
     -d $'{"id":"3","location":[2,2],"board":[["O","X","O"],["X","O","X"],["X","O"," "]],"gameStatus":"INPROGRESS"}'


# Get the state of game #1
echo "\n Get the state of game 1"
curl "http://localhost:8087/tinytictactoe/games/1"

echo "\n Get the state of game 2"
curl "http://localhost:8087/tinytictactoe/games/2"

echo "\n Get the state of game 3"
curl "http://localhost:8087/tinytictactoe/games/3"

echo "\n Get the state of game 4"
curl "http://localhost:8087/tinytictactoe/games/4"


# Delete game #2 and game #4
echo "\n Delete game #2"
curl -X "DELETE" "http://localhost:8087/tinytictactoe/games/2"

echo "\n Delete game #4"
curl -X "DELETE" "http://localhost:8087/tinytictactoe/games/2"


# Get a list of all the games
echo "\n all games in system -- should only have game #1 and game #3"
curl "http://localhost:8087/tinytictactoe/games"

