package com.cevaris.authme.auth.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthNewResponse {

  private boolean success;
  private String message;

  private AuthNewRequest request;

}
