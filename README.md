## TinyTicTacToe
------------------

## Overview
TinyTicTacToe is a RESTful API implementing a very simple TicTacToe game service. The service is stateless and does not maintain a state for the purpose of playing the game. (However, I thought it would be nice to maintain a list of all the games in progress, or already played, so simple in-memory map is used to maintain the games. It is *not* used for the purposes of making a move.)

## Description
A “game” is created by the tinytictactoe service with an empty board and is sent to the “player”. At this point the game is marked ‘INPROGRESS’. The player simply adds a location specified as a [row, column] specified as integers and sends it to the service. 

The service checks if the move is valid (i.e. the location is not out of bounds or the square is not already occupied), and records it. It then checks if the user’s move is a win or a draw (board fully covered and the user hasn’t won.) After the user’s move, the game may be ‘INPROGRESS’, ‘PLAYERWON’ or ‘DRAW’

If the user hasn’t won, and it is not a draw, then the service enters its move. And again, checks if the service has won or if it is a draw.

Finally, the new board representing the new status of the game is returned to the user. After a turn, the board may be either ‘INPROGRESS’, ‘PLAYERWON’, ‘SERVICEWON’ or ‘DRAW’.

The service handles exceptions by returning the appropriate HttpStatus code. For this exercise, the only HTTP status codes returned are 200 (OK), 400 (Bad Request), 404 (Not Found), 409 (Conflict).

## Technologies Used
* [Spring Boot](http://projects.spring.io/spring-boot)
* [Spring Framework](http://projects.spring.io/spring-framework)
* [Jersey](https://jersey.java.net/)
* [Log4j2](http://logging.apache.org/log4j/2.x/)
* [SpringFox](https://springfox.github.io/springfox/) (for Swagger integration)
* [Docker](https://www.docker.com/)

##  Running the service locally

### Clone the git repo
`git clone https://github.com/alotia/tiny-tictactoe.git`

### Build and run the Spring Boot application

Thanks to Spring Boot, running the application is easy. Simply issue the following Maven command.

`mvn spring-boot:run`

Maven will fetch all the necessary dependencies and will compile and run the unit tests and start the application. By default, the application runs on port *8087* and with a server context set to */tinytictactoe*. These properties are specified in `src/main/resources/application.properties` file.

#### Running a sample scenario

A collection of `curl` commands have been packaged into a shell script: `tiny-ttt_curl_calls.sh`

Once the service is started, open a new terminal and run this script to create some games and have it issue some sample commands against the service.

#### Running a docker image

A docker image `alotia/tiny-tictactoe`  of the Spring Boot application has been created and can be fetched from Docker Cloud. (ps. I just learned about Docker as part of doing this exercise, so there may be some glitches here!)

### View the API document

The API is documented via swagger. Once the service is up, the API documentation can be viewed at:
`http://localhost:8087/tinytictactoe/swagger-ui.html`

## Description of the game elements

Service: The TinyTicTacToe service. When the service makes a move, it always uses ‘O’ as its marker.

Player: The opponent, probably a human player. The player’s marker is always ‘X’. For simplicity, the Player always makes the first move. (The createGame API has a hook to allow the service to make the first move, but it has not been implemented.)

Here's a sample game that does not have any recorded moves:

```
{
  "id": "1",
  "location": [0, 0],
  "board": [ [ " ", " ", " "], [" ", " ", " "], [" ", " ", " " ] ],
  "gameStatus": "INPROGRESS"
}
```



`id`: This was added as a nice-to-have feature to start multiple games and be able to list all the games in progress. This allows a player to make a move for a specific game. This gameId is also included in the schema, but this id is not used to look up the game. It is only used to validate that the {gameId} in the path parameter matches the id in the JSON schema. 

`location`: It is a two-element array of integers representing the row and column of where to make the move.

`board`: A 2-D NxN array of characters. By default, it is a 3x3 board and is initialized to all a space character (‘ ‘) in each cell. The only allowed characters are: ' ', ‘X’ and ‘0’.

`gameStatus`: This can be one of the following four values: 'INPROGRESS', 'PLAYERWON', 'SERVICEWON' or 'DRAW'.

### Assumptions and TODOs

Since this is not a "multiplayer" game where users are competing against one another, authentication was not required. The service does not care who sends it a request, it merely handles as a request for a starting a new game, deleting an existing game, or playing a move in a game.

I decided to implement a player making a move as a 'POST' rather than a 'PUT', since a move is not idempotent. Once a square has been marked, you cannot mark it again, and also that once a player has played a turn, the service also plays a turn, so the board is not the same between subsequent requests.

*More unit tests are needed.*








