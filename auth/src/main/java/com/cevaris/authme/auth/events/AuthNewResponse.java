package com.cevaris.authme.auth.events;

public class AuthNewResponse {

  private String id;
  private AuthNewRequest request;

  public String getId() {
    return id;
  }

  public void setIdentity(String id) {
    this.id = id;
  }

  public void setRequest(AuthNewRequest request) {
    this.request = request;
  }
}
