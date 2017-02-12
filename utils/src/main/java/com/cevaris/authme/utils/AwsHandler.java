package com.cevaris.authme.utils;

import java.util.List;

import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

public abstract class AwsHandler<A, B> extends RequestHandler2 {

  private Injector localInjector;

  /**
   * Entry point into aws lambda handler
   *
   * @param event generic request event
   * @param c     aws runtime context
   * @return generic response event
   */
  protected abstract B handler(A event, Context c);

  /**
   * Load up any modules at runtime
   */
  protected List<Module> modules() {
    return Lists.newArrayList();
  }

  /**
   * getInstance wrapper around guice injector
   *
   * @param type class type to be loaded
   * @param <T>  class type to be loaded
   * @return instance of class type
   */
  protected <T> T getInstance(Class<T> type) {
    if (localInjector == null) {
      List<Module> handlerModules = modules();
      if (handlerModule() != null) {
        handlerModules.add(handlerModule());
      }
      setInjector(Guice.createInjector(Stage.PRODUCTION, handlerModules));
    }
    return localInjector.getInstance(type);
  }

  /**
   * Swap out runtime injector for test/mocked loaded injector
   *
   * @param injector
   */
  @VisibleForTesting
  public void setInjector(Injector injector) {
    this.localInjector = injector;
  }

  /**
   * Handler specific inject module
   *
   * @return com.google.inject.Module
   */
  protected Module handlerModule() {
    return null;
  }

}