package org.lotia.example.tinytictactoe.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value = "/healthCheck")
@Api(value = "healthCheck", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthCheck {
	
	private final Logger logger = LoggerFactory.getLogger(HealthCheck.class);
			
	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation("Returns the result from service's health check. NOT implemented")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Object.class)})
	public ResponseEntity<?> healthCheck() {
		
		logger.debug("HealthCheck:healthCheck called");
		
		// TODO: Implement health checks
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
