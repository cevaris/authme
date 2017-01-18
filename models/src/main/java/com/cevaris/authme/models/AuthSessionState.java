package com.cevaris.authme.models;

public enum AuthSessionState {
  ACCEPTED("accepted"),
  REJECTED("rejected"),
  OPEN("open");

  private final String state;

  AuthSessionState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }
}
