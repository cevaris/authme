package com.cevaris.authme.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.models.IdentityType;
import com.cevaris.authme.test.utils.TestContext;

import org.junit.Assert;
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
    AuthNewRequest request = AuthNewRequest.builder()
        .identity("Helfinch1979@einrot.com")
        .identityType(IdentityType.EMAIL.getValue())
        .build();

    AuthNewResponse response = handler.handler(request, context);

    Assert.assertEquals(true, response.isSuccess());
    Assert.assertEquals(request, response.getRequest());
    Assert.assertNull(response.getMessage());
  }

  @Before
  public void setUp() throws Exception {
    handler = new AuthNewHandler();
  }
}