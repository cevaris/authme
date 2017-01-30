package com.cevaris.authme.modules.aws;


import com.cevaris.authme.utils.AppEnv;
import com.cevaris.authme.utils.Const;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppModule implements Module {
  @Singleton
  @Provides
  public AppEnv providesServerEnvironment() {
    String rawEnv = System.getenv().get(Const.APP_ENV);
    if (rawEnv == null) {
      throw new IllegalArgumentException(String.format("Missing %s header", Const.APP_ENV));
    } else {
      return AppEnv.parse(rawEnv);
    }
  }

  public void configure(Binder binder) {
  }
}
