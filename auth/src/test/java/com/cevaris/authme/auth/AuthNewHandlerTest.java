package com.cevaris.authme.auth;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.lambda.runtime.Context;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.models.storage.dynamodb.AuthSessionDynamoDB;
import com.cevaris.authme.test.utils.TestContext;
import com.cevaris.authme.utils.DateTimeUtils;
import com.cevaris.authme.utils.mail.LoggerMailer;
import com.cevaris.authme.utils.mail.Mailer;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

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
  private Mailer mailer = new LoggerMailer();

  private DynamoDB mockDynamoDb = mock(DynamoDB.class);
  private Table mockTable = mock(Table.class);
  private TableDescription mockTableDesc = mock(TableDescription.class);


  @Test
  public void testSuccessful() {
    AuthNewRequest request = new AuthNewRequest();
    request.setEmail("test@example.com");

    when(mockDynamoDb.getTable("authme.authsession.dev"))
        .thenReturn(mockTable);

    when(mockTable.describe())
        .thenReturn(mockTableDesc);

    DateTimeUtils.setCurrentMillisFixed(1485106610000L);
    AuthNewResponse response = handler.handler(request, context);
    assertEquals("6498bb79e7f47b8dc00e64f5f263b54d", response.getId());
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

  class TestModule implements Module {
    public void configure(Binder binder) {
      binder.bind(DynamoDB.class).toInstance(mockDynamoDb);
      binder.bind(Mailer.class).toInstance(mailer);
      binder.bind(String.class)
          .annotatedWith(Names.named(AuthSessionDynamoDB.TABLE_NAME))
          .toInstance("authme.authsession.dev");
    }

  }
}