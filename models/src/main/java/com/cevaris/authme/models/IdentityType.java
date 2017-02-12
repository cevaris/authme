package com.cevaris.authme.models;

import com.google.common.base.Preconditions;

public enum IdentityType {
  EMAIL("email"),
  PHONE("phone");

  private final String value;

  IdentityType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static IdentityType withValue(String value) {
    Preconditions.checkNotNull(value);
    for (IdentityType identityType : IdentityType.values()) {
      if (identityType.getValue().equalsIgnoreCase(value)) {
        return identityType;
      }
    }
    throw new IllegalArgumentException(String.format("enum %s not found", value));
  }
}
