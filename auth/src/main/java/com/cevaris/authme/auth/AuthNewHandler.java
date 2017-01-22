package com.cevaris.authme.auth;


import com.amazonaws.Request;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.modules.aws.AwsDynamoDbModule;
import com.cevaris.authme.utils.AwsHandler;
import com.cevaris.authme.utils.HashUtils;
import com.google.inject.Guice;
import com.google.inject.Stage;


public class AuthNewHandler extends AwsHandler<AuthNewRequest, AuthNewResponse> {

  @Override
  public AuthNewResponse handler(AuthNewRequest event, Context context) {
    LambdaLogger logger = context.getLogger();
    DynamoDB client = injector.getProvider(DynamoDB.class).get();

    Table table = client.getTable("authme.authsession.dev");
    logger.log(table.getTableName());

    logger.log(String.format("%s %s - %s\n", context.getFunctionName(), context.getAwsRequestId(), event));

    AuthNewResponse response = new AuthNewResponse();
    response.setReceipt(HashUtils.timedHash(event.getEmail()));

    return response;
  }

  @Override
  public void beforeRequest(Request<?> request) {
    if (injector == null) {
      setInjector(Guice.createInjector(Stage.PRODUCTION, new AwsDynamoDbModule()));
    }
  }
}
