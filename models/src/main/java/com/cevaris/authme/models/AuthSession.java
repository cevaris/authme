package com.cevaris.authme.models;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthSession {
  public final static String IDX_AUTH_TOKEN = "idx_auth_token";

  @DynamoDBHashKey()
  private String identity;

  @DynamoDBIndexHashKey(globalSecondaryIndexName = IDX_AUTH_TOKEN, attributeName = "auth_token")
  private String authToken;

  @DynamoDBAttribute(attributeName = "identity_type")
  private String identityType;

  @DynamoDBAttribute(attributeName = "created_at")
  private Date createdAt;

  @DynamoDBAttribute(attributeName = "auth_session_state")
  private String authSessionState;

}
