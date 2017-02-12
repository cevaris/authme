package com.cevaris.authme.models.storage.dynamodb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.cevaris.authme.models.AuthSession;
import com.google.inject.Inject;

public class AuthSessionDynamoDB implements DynamoDbRepository<AuthSession> {

  private Logger logger = Logger.getLogger(getClass().getName());
  public static final String TABLE_NAME = "AUTHME_AUTH_SESSION_TABLE";
  private final DynamoDBMapper client;
  private final String tableName;

  @Inject
  @Singleton
  AuthSessionDynamoDB(AmazonDynamoDBClient amazonDynamoDBClient, @Named(TABLE_NAME) String tableName) {
    DynamoDBMapperConfig config = DynamoDBMapperConfig
        .builder()
        .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName))
        .build();

    this.client = new DynamoDBMapper(amazonDynamoDBClient, config);
    this.tableName = tableName;

    final ProvisionedThroughput defaultProvision = new ProvisionedThroughput()
        .withReadCapacityUnits(5L)
        .withWriteCapacityUnits(5L);

    try {
      CreateTableRequest createTableRequest = this.client
          .generateCreateTableRequest(AuthSession.class, config)
          .withProvisionedThroughput(defaultProvision);

      // need to add provisions to global secondary indexes as well
      createTableRequest.getGlobalSecondaryIndexes()
          .forEach(p -> p.setProvisionedThroughput(defaultProvision));

      TableUtils.createTableIfNotExists(amazonDynamoDBClient, createTableRequest);
      TableUtils.waitUntilActive(amazonDynamoDBClient, tableName);

      logger.info(String.format("created/found table %s", tableName));
    } catch (InterruptedException e) {
      logger.log(Level.SEVERE, String.format("failed to create table %s", tableName), e);
    }
  }

  public AuthSession create(AuthSession x) {
    client.save(x);
    return x;
  }

  public AuthSession get(Object key) {
    return null;
  }

  public Object delete(Object x) {
    return null;
  }

}
