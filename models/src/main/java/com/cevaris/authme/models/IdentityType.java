package com.cevaris.authme.models;

public enum IdentityType {
  EMAIL("email"),
  PHONE("phone");

  private final String state;

  IdentityType(String state) {
    this.state = state;
  }

  public String getName() {
    return state;
  }

}
