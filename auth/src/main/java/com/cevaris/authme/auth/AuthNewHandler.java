package com.cevaris.authme.auth;


import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.models.storage.dynamodb.AuthSessionDynamoDB;
import com.cevaris.authme.modules.aws.AppModule;
import com.cevaris.authme.modules.aws.AwsDynamoDbModule;
import com.cevaris.authme.utils.AwsHandler;
import com.cevaris.authme.utils.DateTimeUtils;
import com.cevaris.authme.utils.HashUtils;
import com.cevaris.authme.utils.mail.Mailer;
import com.google.common.collect.Lists;
import com.google.inject.Module;
import org.joda.time.DateTime;

import java.util.List;


public class AuthNewHandler extends AwsHandler<AuthNewRequest, AuthNewResponse> {
  @Override
  public List<Module> modules() {
    return Lists.newArrayList(new AppModule(), new AwsDynamoDbModule());
  }

  @Override
  public AuthNewResponse handler(AuthNewRequest event, Context context) {
    LambdaLogger logger = context.getLogger();
    DynamoDB client = getInstance(DynamoDB.class);
    Mailer mailConfig = getInstance(Mailer.class);

    String tableName = System.getenv(AuthSessionDynamoDB.TABLE_NAME);
    Table table = client.getTable(tableName);
    table.describe(); // kick off description request
    logger.log(table.getDescription().getKeySchema().toString());

    logger.log(String.format("%s %s - %s\n", context.getFunctionName(), context.getAwsRequestId(), event));

    DateTime createdAt = DateTimeUtils.now();

    mailConfig.send(event.getEmail(), "Auth Me Request", "<a href=\"https://auth-me.com/verify?token=abcdefg\">Click Me</a>");

    AuthNewResponse response = new AuthNewResponse();
    response.setReceipt(HashUtils.timedHash(event.getEmail(), createdAt));

    return response;
  }

}
