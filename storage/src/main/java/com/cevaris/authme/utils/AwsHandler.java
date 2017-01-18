package com.cevaris.authme.utils;

import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.lambda.runtime.Context;

public abstract class AwsHandler<A, B> extends RequestHandler2 {
  public abstract B handler(A event, Context c);
}