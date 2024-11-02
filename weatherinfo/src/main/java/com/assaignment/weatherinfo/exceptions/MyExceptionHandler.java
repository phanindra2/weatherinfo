package com.assaignment.weatherinfo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class MyExceptionHandler {
	
	@ExceptionHandler(InvalidRequest.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleInvalidArguments(InvalidRequest ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(FutureDateException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleInvalidArgumentsFutureDate(FutureDateException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(ExceptionFromOpenApi.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> handleExceptiionFromOpenWeatherAPi(ExceptionFromOpenApi ex){
		return ResponseEntity.internalServerError().body(ex.getMessage());
	}
	@ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body("Error: " + ex.getMessage());
    }

}
