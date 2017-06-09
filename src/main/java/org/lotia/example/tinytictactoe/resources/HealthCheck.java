package org.lotia.example.tinytictactoe.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/healthCheck")
public class HealthCheck {
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> healthCheck() {
		// TODO: Implement health checks
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
