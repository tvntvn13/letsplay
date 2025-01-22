package com.tvntvn.letsplay.model;

public class ResponseModel<T> {
  private Integer status;
  private String detail;
  private T payload;
  
  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }
  public String getDetail() {
    return detail;
  }
  public void setDetail(String detail) {
    this.detail = detail;
  }
  public T getPayload() {
    return payload;
  }
  public void setPayload(T payload) {
    this.payload = payload;
  }
}
