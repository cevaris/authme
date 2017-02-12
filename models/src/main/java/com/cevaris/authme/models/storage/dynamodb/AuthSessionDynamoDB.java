package com.cevaris.authme.models.storage.dynamodb;

import javax.inject.Named;
import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.cevaris.authme.models.AuthSession;
import com.google.inject.Inject;

public class AuthSessionDynamoDB implements DynamoDbRepository<AuthSession> {

  public static final String TABLE_NAME = "AUTHME_AUTH_SESSION_TABLE";
  private final DynamoDB client;
  private final String tableName;

  @Inject
  @Singleton
  AuthSessionDynamoDB(DynamoDB client, @Named(TABLE_NAME) String tableName) {
    this.client = client;
    this.tableName = tableName;
  }

  public Table getTable() {
    return null;
  }

  public AuthSession create(AuthSession x) {
    return null;
  }

  public AuthSession get(Object key) {
    return null;
  }

  public Object delete(Object x) {
    return null;
  }
}
