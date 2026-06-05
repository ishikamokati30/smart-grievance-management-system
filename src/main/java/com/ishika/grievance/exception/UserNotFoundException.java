package com.ishika.grievance.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String msg) {
	  super(msg);
  }
}
