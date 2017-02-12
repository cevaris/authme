package com.cevaris.authme.aws.modules;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.google.inject.Binder;
import com.google.inject.Module;

public class AwsCredentialsModule implements Module {

  public void configure(Binder binder) {
    binder.bind(AWSCredentialsProvider.class).to(EnvironmentVariableCredentialsProvider.class);
  }

}
