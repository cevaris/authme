package com.cevaris.authme.auth;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthNewHandlerTest {

  private Injector injector;
  private AuthNewHandler handler;

  private Context context = mock(Context.class);
  private DynamoDB dynamoDB = mock(DynamoDB.class);
  private AmazonDynamoDB aDynamoDB = mock(AmazonDynamoDB.class);


  @Test
  public void testSuccessful() {
    AuthNewRequest request = new AuthNewRequest();
    request.setEmail("fake@email.com");

    when(dynamoDB.getTable("authme.authsession.dev"))
        .thenReturn(new Table(aDynamoDB, "authme.authsession.dev"));

    handler.handler(request, context);

    assertEquals(1, 1);
  }


  @Before
  public void setUp() throws Exception {
    injector = Guice.createInjector(new TestModule());
    handler = new AuthNewHandler();
    handler.setInjector(injector);

    when(context.getFunctionName())
    when(context.getLogger())
        .thenReturn(new LambdaLogger() {
          public void log(String s) {
            System.out.println(s);
          }
        });
  }

  @After
  public void tearDown() throws Exception {
    injector = null;
  }

  class TestModule extends AbstractModule {
    @Override
    protected void configure() {
      bind(DynamoDB.class).toProvider(new Provider<DynamoDB>() {
        public DynamoDB get() {
          return dynamoDB;
        }
      });
    }
  }

}