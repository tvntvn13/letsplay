package com.tvntvn.letsplay.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseModel<T> {
  private Integer status;
  private String detail;
  private T payload;

  public ResponseModel(T payload, String detail, Integer status) {
    this.payload = payload;
    this.status = status;
    this.detail = detail;
  }
}
