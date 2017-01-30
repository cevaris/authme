package com.cevaris.authme.modules.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.model.Region;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class AwsDynamoDbModule implements Module {
  public void configure(Binder binder) {
    binder.bind(AWSCredentialsProvider.class).to(EnvironmentVariableCredentialsProvider.class);
  }

  @Provides
  @Singleton
  DynamoDB providesDynamoDB(AmazonDynamoDBClient client) {
    return new DynamoDB(client);
  }

  @Provides
  @Singleton
  AmazonDynamoDBClient providesDynamoDB(AWSCredentialsProvider credentialsProvider) {
    return new AmazonDynamoDBClient(credentialsProvider);
  }

}
