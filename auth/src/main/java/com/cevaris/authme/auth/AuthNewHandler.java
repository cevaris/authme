package com.cevaris.authme.auth;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.utils.AwsHandler;
import com.cevaris.authme.utils.HashUtils;


public class AuthNewHandler extends AwsHandler<AuthNewRequest, AuthNewResponse> {

  @Override
  public AuthNewResponse handler(AuthNewRequest event, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log(String.format("%s %s - %s\n", context.getFunctionName(), context.getAwsRequestId(), event));

    AuthNewResponse response = new AuthNewResponse();
    response.setReceipt(HashUtils.toString(event.getEmail()));

    return response;
  }

}
