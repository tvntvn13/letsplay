package com.tvntvn.letsplay.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.model.User;

@Component
public class ResponseFormatter {

  ResponseFormatter() {}

  public ResponseEntity<Object> format(String message, HttpStatus status) {
    return new ResponseEntity<Object>(message, status);
  }

  public ResponseEntity<Object> format(String message, String userInfo, HttpStatus status) {
    return new ResponseEntity<Object>(message + " " + userInfo, status);
  }

  public ResponseEntity<Object> format(User user, HttpStatus status) {
    return new ResponseEntity<Object>(user, status);
  }

  public ResponseEntity<Object> format(Product product, HttpStatus status) {
    return new ResponseEntity<Object>(product, status);
  }

  public ResponseEntity<Object> format(List<Product> list, HttpStatus status) {
    return new ResponseEntity<Object>(list, status);
  }
}
