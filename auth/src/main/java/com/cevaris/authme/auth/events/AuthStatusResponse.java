package com.cevaris.authme.auth.events;

public class AuthStatusResponse {
  private String receipt;
  private String state;

  public String getReceipt() {
    return receipt;
  }

  public void setReceipt(String receipt) {
    this.receipt = receipt;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
