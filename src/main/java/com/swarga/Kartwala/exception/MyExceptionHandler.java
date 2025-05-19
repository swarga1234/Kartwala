package com.swarga.Kartwala.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> myMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
		
		Map<String, String> response= new HashMap<>();
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		for(ObjectError error: allErrors) {
			String fieldName="";
			if(error instanceof FieldError) {
				//It is FieldError
				fieldName= ((FieldError) error).getField();
			}
			else {
				//It is global error
				fieldName = error.getObjectName();
			}
			String errorMsg= error.getDefaultMessage();
			response.put(fieldName, errorMsg);
		}
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> myResourceNotFoundExceptionHandler(ResourceNotFoundException e){
		String message=e.getMessage();
		return new ResponseEntity<String>(message,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<String> myAPIExceptionHandler(APIException e){
		String message=e.getMessage();
		return new ResponseEntity<String>(message,HttpStatus.BAD_REQUEST);
	}

}
