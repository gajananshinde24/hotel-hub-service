package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
	
	 private final String message;

	public ResourceNotFoundException(String message) {
		super(message);
		this.message = message;
	}
	 
	 

}
