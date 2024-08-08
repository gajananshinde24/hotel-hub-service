package com.example.demo.model.response;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
	
	public String generateJsonReponse(String title, String message, int statusCode) {
		return "{"
		        + "\"" + title + "\": \"" + message + "\", "
		        + "\"statusCode\": " + statusCode + ", "
		        + "\"zonedDateTime\": \"" + ZonedDateTime.now() + "\""
		        + "}";

	}

}
