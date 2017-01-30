package com.cevaris.authme.utils;

import com.google.common.base.Preconditions;

public enum AppEnv {
  PROD("prod"),
  DEV("dev"),
  TEST("test");

  private String env = null;

  AppEnv(String text) {
    this.env = text;
  }

  public static AppEnv parse(String text) {
    Preconditions.checkNotNull(text);

    for (AppEnv candidate : AppEnv.values()) {
      if (text.trim().equalsIgnoreCase(candidate.env)) {
        return candidate;
      }
    }

    throw new IllegalArgumentException(String.format("Invalid %s: %s", Const.APP_ENV, text));
  }

  @Override
  public String toString() {
    return env;
  }

}

