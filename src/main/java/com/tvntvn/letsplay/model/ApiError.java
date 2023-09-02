package com.tvntvn.letsplay.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ApiError {
  @Getter @Setter private Integer status;
  @Getter @Setter private String payload;
  @Getter @Setter private String detail;

  public ApiError(Integer status, String message, Throwable exception) {
    this.status = status;
    this.detail = message;
    this.payload = exception.getLocalizedMessage();
  }
}
