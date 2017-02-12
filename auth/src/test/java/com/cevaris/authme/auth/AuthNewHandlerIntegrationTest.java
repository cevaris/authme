package com.cevaris.authme.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.test.utils.TestContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class AuthNewHandlerIntegrationTest {
  @Rule
  public final EnvironmentVariables envVars = new EnvironmentVariables();

  private AuthNewHandler handler;

  private Context context = new TestContext("auth-new");

  @Test
  public void testSuccessful() {
    AuthNewRequest request = new AuthNewRequest();
    request.setEmail("Helfinch1979@einrot.com");
    // http://www.fakemailgenerator.com/inbox/einrot.com/helfinch1979/

    AuthNewResponse response = handler.handler(request, context);
  }

  @Before
  public void setUp() throws Exception {
    handler = new AuthNewHandler();
  }
}