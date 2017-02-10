package com.cevaris.authme.aws.modules;

import javax.inject.Singleton;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.AwsEnvVarOverrideRegionProvider;
import com.amazonaws.regions.AwsRegionProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMSClient;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

public class AwsKmsModule implements Module {

  @Provides
  @Singleton
  AWSKMSClient providesAWSKMSClient(
      AwsRegionProvider awsRegionProvider,
      AWSCredentialsProvider credentialsProvider
  ) {
    return new AWSKMSClient(credentialsProvider.getCredentials())
        .withRegion(Regions.fromName(awsRegionProvider.getRegion()));
  }

  public void configure(Binder binder) {
    binder.bind(AwsRegionProvider.class).to(AwsEnvVarOverrideRegionProvider.class);
  }
}
