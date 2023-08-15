package com.tvntvn.letsplay.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class ApiError {
  @Getter @Setter private HttpStatus status;
  @Getter @Setter private String message;
  @Getter @Setter private String debugMessage;

  public ApiError(HttpStatus status, String message, Throwable exception) {
    this.status = status;
    this.message = message;
    this.debugMessage = exception.getLocalizedMessage();
  }
}
