package com.swarga.Kartwala.exception;

/*
 * This is a generic exception to handle bad API requests
 */
public class APIException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public APIException() {
		
	}

	public APIException(String message) {
		super(message);
	}
	
	

}
