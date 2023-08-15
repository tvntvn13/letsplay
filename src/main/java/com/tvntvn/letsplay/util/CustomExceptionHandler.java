package com.tvntvn.letsplay.util;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tvntvn.letsplay.model.ApiError;

@ControllerAdvice
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
}
