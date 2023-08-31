package com.tvntvn.letsplay.model;

import lombok.Getter;
import lombok.Setter;

public class ApiError {
  @Getter @Setter private Integer status;
  @Getter @Setter private String payload;
  @Getter @Setter private String detail;

  public ApiError(Integer status, String message, Throwable exception) {
    this.status = status;
    this.payload = message;
    this.detail = exception.getLocalizedMessage();
  }
}
