package com.cevaris.authme.aws.modules;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.google.inject.Binder;
import com.google.inject.Module;

public class AwsCredentialsModule implements Module {

//  @Singleton
//  @Provides
//  public AppEnv providesServerEnvironment() {
//    String rawEnv = System.getenv().get(Const.APP_ENV);
//    if (rawEnv == null) {
//      throw new IllegalArgumentException(String.format("Missing %s header", Const.APP_ENV));
//    } else {
//      return AppEnv.parse(rawEnv);
//    }
//  }

  public void configure(Binder binder) {
    binder.bind(AWSCredentialsProvider.class).to(EnvironmentVariableCredentialsProvider.class);
  }

}
