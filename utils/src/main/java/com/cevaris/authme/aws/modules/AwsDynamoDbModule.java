package com.cevaris.authme.aws.modules;

import javax.inject.Singleton;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.AwsRegionProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

public class AwsDynamoDbModule implements Module {

  @Provides
  @Singleton
  DynamoDB providesDynamoDB(AmazonDynamoDBClient client) {
    return new DynamoDB(client);
  }

  @Provides
  @Singleton
  AmazonDynamoDBClient providesDynamoDB(AWSCredentialsProvider credentialsProvider, AwsRegionProvider awsRegionProvider) {
    return new AmazonDynamoDBClient(credentialsProvider)
        .withRegion(Regions.fromName(awsRegionProvider.getRegion()));
  }

  public void configure(Binder binder) {
  }
}
