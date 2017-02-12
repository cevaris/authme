package com.cevaris.authme.auth;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.models.AuthSession;
import com.cevaris.authme.models.AuthSessionState;
import com.cevaris.authme.models.IdentityType;
import com.cevaris.authme.models.storage.dynamodb.AuthSessionDynamoDB;
import com.cevaris.authme.test.utils.TestContext;
import com.cevaris.authme.utils.DateTimeUtils;
import com.cevaris.authme.utils.PropertyStore;
import com.cevaris.authme.utils.mail.Mailer;
import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthNewHandlerTest {

  private AuthNewHandler handler;

  private Context context = new TestContext("auth-new");

  private Mailer mockMailer = mock(Mailer.class);
  private PropertyStore mockPropertyStore = mock(PropertyStore.class);
  private AuthSessionDynamoDB mockAuthSessionDynamoDB = mock(AuthSessionDynamoDB.class);
  private AuthSession expected;

  @Before
  public void setUp() throws Exception {
    handler = new AuthNewHandler() {
      @Override
      public List<Module> modules() {
        return Lists.newArrayList(
            (Module) new TestModule()
        );
      }

      @Override
      protected Module handlerModule() {
        return null;
      }
    };
  }

  @After
  public void tearDown() throws Exception {
    DateTimeUtils.setCurrentMillisFixed(DateTime.now());
  }

  @Test
  public void testSuccessful() {
    DateTimeUtils.setCurrentMillisFixed(1485106610000L);

    AuthNewRequest request = AuthNewRequest.builder()
        .identity("test@example.com")
        .identityType(IdentityType.EMAIL.getValue())
        .build();

    expected = AuthSession.builder()
        .identity("test@example.com")
        .authToken("167c42975d473e5550c49ded8c17392d")
        .identityType(IdentityType.EMAIL.getValue())
        .createdAt(new Date(1485106610000L))
        .authSessionState(AuthSessionState.OPEN.getValue())
        .build();

    when(mockPropertyStore.get(AuthNewHandler.SALT_AUTH_TOKEN))
        .thenReturn("7H15 15 my 54L7");

    // Does not match because of java.util.Date
    when(mockAuthSessionDynamoDB.create(any(AuthSession.class)))
        .thenReturn(expected);

    AuthNewResponse response = handler.handler(request, context);

    assertEquals(true, response.isSuccess());
    assertEquals(request, response.getRequest());

    verify(mockMailer, times(1))
        .send(anyString(), anyString(), anyString());
  }

  class TestModule implements Module {
    public void configure(Binder binder) {
      binder.bind(Mailer.class).toInstance(mockMailer);
      binder.bind(PropertyStore.class).toInstance(mockPropertyStore);
      binder.bind(AuthSessionDynamoDB.class).toInstance(mockAuthSessionDynamoDB);
      binder.bind(String.class)
          .annotatedWith(Names.named(AuthSessionDynamoDB.TABLE_NAME))
          .toInstance("authme.authsession.dev");
    }

  }
}