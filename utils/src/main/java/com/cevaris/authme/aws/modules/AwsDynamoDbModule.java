package com.cevaris.authme.aws.modules;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class AwsDynamoDbModule implements Module {

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

  public void configure(Binder binder) {
  }
}
