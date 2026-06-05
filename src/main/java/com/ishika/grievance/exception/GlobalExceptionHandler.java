package com.ishika.grievance.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	  @ExceptionHandler(MethodArgumentNotValidException.class)
	    public Map<String,String> handleValidation(
	            MethodArgumentNotValidException ex){

	        Map<String,String> errors = new HashMap<>();

	        ex.getBindingResult()
	          .getFieldErrors()
	          .forEach(error ->
	              errors.put(
	                 error.getField(),
	                 error.getDefaultMessage()));

	        return errors;
	    }
	  
	  @ExceptionHandler(UserNotFoundException.class)
	  public Map<String,String> handleUserNotFound(
	          UserNotFoundException ex){

	      Map<String,String> map = new HashMap<>();

	      map.put("error", ex.getMessage());

	      return map;
	  }
	}
