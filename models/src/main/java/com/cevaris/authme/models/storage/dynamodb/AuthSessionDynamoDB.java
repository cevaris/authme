package com.cevaris.authme.models.storage.dynamodb;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.Inject;

import javax.inject.Singleton;

public class AuthSessionDynamoDB {

  public static final String TABLE_NAME = "AUTHME_AUTH_SESSION_TABLE";

  @Inject
  @Singleton
  AuthSessionDynamoDB(DynamoDB client) {
  }
}
