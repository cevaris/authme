package com.cevaris.authme.modules.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class AwsDynamoDbModule extends AbstractModule {

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

  @Override
  protected void configure() {
    bind(AWSCredentialsProvider.class).to(EnvironmentVariableCredentialsProvider.class);
  }
}