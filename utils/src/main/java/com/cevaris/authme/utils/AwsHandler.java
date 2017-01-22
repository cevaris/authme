package com.cevaris.authme.utils;

import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.inject.Injector;

public abstract class AwsHandler<A, B> extends RequestHandler2 {
  public abstract B handler(A event, Context c);

  protected Injector injector;

  public void setInjector(Injector injector) {
    this.injector = injector;
  }
}