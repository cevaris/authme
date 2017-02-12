package com.cevaris.authme.auth;


import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.cevaris.authme.app.modules.AppModule;
import com.cevaris.authme.auth.events.AuthNewRequest;
import com.cevaris.authme.auth.events.AuthNewResponse;
import com.cevaris.authme.aws.modules.AwsCredentialsModule;
import com.cevaris.authme.aws.modules.AwsDynamoDbModule;
import com.cevaris.authme.aws.modules.AwsKmsModule;
import com.cevaris.authme.mailgun.modules.MailgunModule;
import com.cevaris.authme.models.AuthSession;
import com.cevaris.authme.models.AuthSessionState;
import com.cevaris.authme.models.IdentityType;
import com.cevaris.authme.models.storage.dynamodb.AuthSessionDynamoDB;
import com.cevaris.authme.models.storage.dynamodb.DynamoDbRepository;
import com.cevaris.authme.utils.AwsHandler;
import com.cevaris.authme.utils.DateTimeUtils;
import com.cevaris.authme.utils.HashUtils;
import com.cevaris.authme.utils.PropertyStore;
import com.cevaris.authme.utils.mail.Mailer;
import com.cevaris.authme.utils.mail.MailgunMailer;
import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

import org.joda.time.DateTime;


public class AuthNewHandler extends AwsHandler<AuthNewRequest, AuthNewResponse> {

  private final static String SALT_AUTH_TOKEN = "salt.auth_token";

  @Override
  public List<Module> modules() {
    return Lists.newArrayList(
        new AppModule(),
        new AwsCredentialsModule(),
        new AwsDynamoDbModule(),
        new AwsKmsModule(),
        new MailgunModule()
    );
  }

  @Override
  public AuthNewResponse handler(AuthNewRequest event, Context context) {
    DynamoDbRepository<AuthSession> client = getInstance(AuthSessionDynamoDB.class);
    Mailer mailConfig = getInstance(Mailer.class);
    PropertyStore propertyStore = getInstance(PropertyStore.class);

    LambdaLogger logger = context.getLogger();
    logger.log(String.format("%s %s - %s\n", context.getFunctionName(), context.getAwsRequestId(), event));

    DateTime createdAt = DateTimeUtils.now();
    String saltAuthToken = propertyStore.get(SALT_AUTH_TOKEN);
    String token = HashUtils.timedHash(String.format("%s/%s", saltAuthToken, event.getEmail()), createdAt);

    String body = String.format("<a href=\"https://auth-me.com/verify?token=%s\">Authorize Me</a>", token);
    mailConfig.send(event.getEmail(), "Auth Me Request", body);

    AuthSession authSession = AuthSession.builder()
        .authSessionState(AuthSessionState.OPEN.getName())
        .authToken(token)
        .createdAt(createdAt.toDate())
        .identity(event.getEmail())
        .identityType(IdentityType.EMAIL.getName())
        .build();

    try {
      client.create(authSession);
      logger.log(String.format("persisted auth_session for %s", authSession.getIdentity()));
    } catch (RuntimeException e) {
      logger.log(String.format("error in saving auth_session for %s: %s", authSession.getIdentity(), e.getMessage()));
    }

    AuthNewResponse response = new AuthNewResponse();
    response.setRequest(event);
    response.setIdentity(token);

    return response;
  }

  @Override
  protected Module handlerModule() {
    return new Module() {
      public void configure(Binder binder) {
        binder.bind(Mailer.class).to(MailgunMailer.class);
        binder.bind(String.class)
            .annotatedWith(Names.named(PropertyStore.PROPERTY_RESOURCE_PATH))
            .toInstance("/enc-test.properties");
        binder.bind(String.class)
            .annotatedWith(Names.named(AuthSessionDynamoDB.TABLE_NAME))
            .toInstance("authme.authsession.test");
      }
    };
  }

}
