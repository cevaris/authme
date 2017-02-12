package com.cevaris.authme.models;

public enum AuthSessionState {
  ACCEPTED("accepted"),
  REJECTED("rejected"),
  OPEN("open");

  private final String value;

  AuthSessionState(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
