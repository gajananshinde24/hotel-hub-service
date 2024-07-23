package com.example.demo.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ApiException handleResourceNotFoundException(ResourceNotFoundException e) {
		
		ApiException apiException = new ApiException();
		apiException.setErrorMessage(e.getMessage());
		apiException.setStatusCode(HttpStatus.NOT_FOUND.value());
		apiException.setZonedDateTime(ZonedDateTime.now());
		return apiException;  
	      
    }
	
	@ExceptionHandler(BookingException.class)
	public ApiException handleBookingException(BookingException e) {
		
		ApiException apiException = new ApiException();
		apiException.setErrorMessage(e.getMessage());
		apiException.setStatusCode(HttpStatus.BAD_REQUEST.value());
		apiException.setZonedDateTime(ZonedDateTime.now());
		return apiException;  
	      
    }
	
	@ExceptionHandler(InvalidRequestException.class)
	public ApiException handleInvalidRequestException(InvalidRequestException e) {
		
		ApiException apiException = new ApiException();
		apiException.setErrorMessage(e.getMessage());
		apiException.setStatusCode(HttpStatus.BAD_REQUEST.value());
		apiException.setZonedDateTime(ZonedDateTime.now());
		return apiException;  
	      
    }
	
	@ExceptionHandler(RuntimeException.class)
	public ApiException handleRuntimeException(RuntimeException e) {
		ApiException apiException = new ApiException();
		apiException.setErrorMessage(e.getMessage());
		apiException.setStatusCode(HttpStatus.BAD_REQUEST.value());
		apiException.setZonedDateTime(ZonedDateTime.now());
		return apiException;  
		
	}
	
	

}
