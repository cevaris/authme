package com.cevaris.authme.auth.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthNewRequest {

  private String identity;

  private String identityType;

  @Override
  public String toString() {
    try {
      return new ObjectMapper().writer().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }
}
