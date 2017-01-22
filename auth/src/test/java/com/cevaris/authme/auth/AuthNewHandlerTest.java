package com.cevaris.authme.auth;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.lambda.runtime.Context;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.utils.DateTimeUtils;
import com.cevaris.authme.utils.testing.TestContext;
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

  private Context context = new TestContext("auth-new");
  private DynamoDB dynamoDB = mock(DynamoDB.class);
  private Table table = mock(Table.class);
  private TableDescription tableDesc = mock(TableDescription.class);


  @Test
  public void testSuccessful() {
    AuthNewRequest request = new AuthNewRequest();
    request.setEmail("fake@email.com");

    when(dynamoDB.getTable("authme.authsession.dev"))
        .thenReturn(table);

    when(table.getDescription())
        .thenReturn(tableDesc);

    DateTimeUtils.setCurrentMillisFixed(1485106610000L);
    AuthNewResponse response = handler.handler(request, context);
    assertEquals(response.getReceipt(), "7de4c76817545649c2b49ef257debb76");
  }


  @Before
  public void setUp() throws Exception {
    injector = Guice.createInjector(new TestModule());
    handler = new AuthNewHandler();
    handler.setInjector(injector);
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