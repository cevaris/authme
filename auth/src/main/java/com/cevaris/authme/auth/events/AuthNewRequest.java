package com.cevaris.authme.auth.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthNewRequest {
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    try {
      return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }
}
