package com.tvntvn.letsplay.util;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tvntvn.letsplay.model.ApiError;

@ControllerAdvice
@Component
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception exception) {
    // HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    HttpStatus status = HttpStatus.FORBIDDEN;
    String message = "An error occurred";
    if (exception instanceof NotFoundException) {
      status = HttpStatus.NOT_FOUND;
      message = "Resource not found";
    } else if (exception instanceof IllegalArgumentException) {
      status = HttpStatus.BAD_REQUEST;
      message = "Illegal arguments";
    } else if (exception instanceof AccessDeniedException
        || exception instanceof PermissionDeniedDataAccessException) {
      status = HttpStatus.FORBIDDEN;
      message = "Access denied";
    }

    ApiError error = new ApiError(status, message, exception);
    return buildResponseEntity(error);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError error) {
    return new ResponseEntity<>(error, error.getStatus());
  }
  // @ExceptionHandler(NotFoundException.class)
  // public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
  //   String message = "resource not found";
  //   ApiError error = new ApiError(HttpStatus.NOT_FOUND, message, exception);
  //   return buildResponseEntity(error);
  // }

  // private ResponseEntity<Object> buildResponseEntity(ApiError error) {
  //   return new ResponseEntity<Object>(error, error.getStatus());
  // }

  // @ExceptionHandler(IllegalArgumentException.class)
  // public ResponseEntity<Object> handleUnauthorized(IllegalArgumentException exception) {
  //   String message = "illegal arguments";
  //   ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, exception);
  //   return buildResponseEntity(error);
  // }

  // @ExceptionHandler(AccessDeniedException.class)
  // public ResponseEntity<Object> handleDenied(AccessDeniedException exception) {
  //   String message = "access denied";
  //   ApiError error = new ApiError(HttpStatus.FORBIDDEN, message, exception);
  //   return buildResponseEntity(error);
  // }

  // @ExceptionHandler(PermissionDeniedDataAccessException.class)
  // public ResponseEntity<Object> handlePermissionDenied(
  //     PermissionDeniedDataAccessException exception) {
  //   String message = "access denied";
  //   ApiError error = new ApiError(HttpStatus.FORBIDDEN, message, exception);
  //   return buildResponseEntity(error);
  // }
}
