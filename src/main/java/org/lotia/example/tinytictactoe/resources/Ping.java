package org.lotia.example.tinytictactoe.resources;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/ping")
@Api(value = "ping", produces = MediaType.APPLICATION_JSON_VALUE)
public class Ping {
	
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation("Returns a \"welcome\" string!")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Welome to TinyTicTacToe", response = Object.class)})
	public ResponseEntity<String> ping() {
		return new ResponseEntity<String>("Welome to TinyTicTacToe", HttpStatus.OK);
	}
}
