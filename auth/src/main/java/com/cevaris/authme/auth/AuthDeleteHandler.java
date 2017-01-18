package com.cevaris.authme.auth;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cevaris.authme.utils.AwsHandler;


public class AuthDeleteHandler extends AwsHandler<String, String> {

  @Override
  public String handler(String event, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log(String.format("%s %s - %s\n", context.getFunctionName(), context.getAwsRequestId(), event));

    return event.toUpperCase();
  }

}
