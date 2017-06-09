package org.lotia.example.tinytictactoe.resources;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ping")
public class Ping {
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> ping() {
		return new ResponseEntity<String>("TinyTicTacToe Service", HttpStatus.OK);
	}
}
