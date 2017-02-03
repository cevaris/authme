package com.cevaris.authme.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.models.storage.dynamodb.AuthSessionDynamoDB;
import com.cevaris.authme.utils.AppEnv;
import com.cevaris.authme.utils.Const;
import com.cevaris.authme.utils.DateTimeUtils;
import com.cevaris.authme.utils.testing.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.Assert.assertEquals;

public class AuthNewHandlerIntegrationTest {
  @Rule
  public final EnvironmentVariables envVars = new EnvironmentVariables();

  private AuthNewHandler handler;

  private Context context = new TestContext("auth-new");

  @Test
  public void testSuccessful() {
    AuthNewRequest request = new AuthNewRequest();
    request.setEmail("fake@email.com");

    DateTimeUtils.setCurrentMillisFixed(1485106610000L);
    AuthNewResponse response = handler.handler(request, context);
    assertEquals(response.getReceipt(), "7de4c76817545649c2b49ef257debb76");
  }

  @Before
  public void setUp() throws Exception {
    handler = new AuthNewHandler();
    handler.beforeRequest(null);
    envVars.set(Const.APP_ENV, AppEnv.TEST.name());
    envVars.set(AuthSessionDynamoDB.TABLE_NAME, "authme.authsession.test");
    envVars.set(.TABLE_NAME, "authme.authsession.test");
  }

  @After
  public void tearDown() throws Exception {
    handler = null;
  }

}