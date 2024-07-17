package com.example.demo.exception;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.Setter;


@Data
@Setter
public class ApiException {

    private String errorMessage;
    private Integer statusCode;
    private ZonedDateTime zonedDateTime;
    
    public void setErrorMessage(String s) {
    	this.errorMessage=s;
    }

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public void setZonedDateTime(ZonedDateTime zonedDateTime) {
		this.zonedDateTime = zonedDateTime;
	}
    
    
}
