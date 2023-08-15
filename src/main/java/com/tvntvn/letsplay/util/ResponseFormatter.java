package com.tvntvn.letsplay.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tvntvn.letsplay.model.User;

@Component
public class ResponseFormatter {

  ResponseFormatter() {}

  public ResponseEntity<Object> format(String message, HttpStatus status) {
    return new ResponseEntity<Object>(message, status);
  }

  public ResponseEntity<Object> format(String message, String user, HttpStatus status) {
    return new ResponseEntity<Object>(message + " " + user, status);
  }

  public ResponseEntity<Object> format(User user, HttpStatus status) {
    return new ResponseEntity<Object>(user, status);
  }
}
