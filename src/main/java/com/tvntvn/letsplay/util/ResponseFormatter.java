package com.tvntvn.letsplay.util;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.model.ResponseModel;
import com.tvntvn.letsplay.model.User;

@Component
public class ResponseFormatter {

  private ResponseModel<String> stringResponse = new ResponseModel<>();
  private ResponseModel<User> userResponse = new ResponseModel<>();
  private ResponseModel<Product> productResponse = new ResponseModel<>();
  private ResponseModel<List<Product>> listResponse = new ResponseModel<>();
  private ResponseModel<List<User>> listUserResponse = new ResponseModel<>();

  private Integer statusCode(HttpStatus status) {
    return Integer.valueOf(status.toString().split(" ")[0]);
  }

  private String statusMessage(HttpStatus status) {
    return status.getReasonPhrase();
  }

  ResponseFormatter() {}

  public ResponseEntity<Object> format(String message, HttpStatus status) {
    stringResponse.setPayload(message);
    stringResponse.setStatus(statusCode(status));
    stringResponse.setDetail(statusMessage(status));
    return new ResponseEntity<Object>(stringResponse, status);
  }

  public ResponseEntity<Object> format(String message, String userInfo, HttpStatus status) {
    stringResponse.setPayload(message + " " + userInfo);
    stringResponse.setStatus(statusCode(status));
    stringResponse.setDetail(statusMessage(status));
    return new ResponseEntity<Object>(stringResponse, status);
  }

  public ResponseEntity<Object> format(User user, HttpStatus status) {
    userResponse.setPayload(user);
    userResponse.setStatus(statusCode(status));
    userResponse.setDetail(statusMessage(status));
    return new ResponseEntity<Object>(userResponse, status);
  }

  public ResponseEntity<Object> format(Product product, HttpStatus status) {
    productResponse.setPayload(product);
    productResponse.setStatus(statusCode(status));
    productResponse.setDetail(statusMessage(status));
    return new ResponseEntity<Object>(productResponse, status);
  }

  public ResponseEntity<Object> formatProductList(List<Product> list, HttpStatus status) {
    listResponse.setPayload(list);
    listResponse.setStatus(statusCode(status));
    listResponse.setDetail(statusMessage(status));
    return new ResponseEntity<Object>(listResponse, status);
  }

  public ResponseEntity<Object> formatUserList(List<User> list, HttpStatus status) {
    listUserResponse.setPayload(list);
    listUserResponse.setStatus(statusCode(status));
    listUserResponse.setDetail(statusMessage(status));
    return new ResponseEntity<Object>(listUserResponse, status);
  }
}
