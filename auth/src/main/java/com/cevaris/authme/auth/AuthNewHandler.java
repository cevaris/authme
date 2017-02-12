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

  public final static String SALT_AUTH_TOKEN = "salt.auth_token";

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
    AuthSessionDynamoDB client = getInstance(AuthSessionDynamoDB.class);
    Mailer mailConfig = getInstance(Mailer.class);
    PropertyStore propertyStore = getInstance(PropertyStore.class);

    LambdaLogger logger = context.getLogger();
    logger.log(String.format("%s %s - %s\n", context.getFunctionName(), context.getAwsRequestId(), event));

    DateTime createdAt = DateTimeUtils.now();
    String saltAuthToken = propertyStore.get(SALT_AUTH_TOKEN);
    String token = HashUtils.timedHash(String.format("%s/%s", saltAuthToken, event.getIdentity()), createdAt);

    AuthSession authSession = AuthSession.builder()
        .authSessionState(AuthSessionState.OPEN.getValue())
        .authToken(token)
        .createdAt(createdAt.toDate())
        .identity(event.getIdentity())
        .identityType(IdentityType.EMAIL.getValue())
        .build();

    AuthNewResponse.AuthNewResponseBuilder responseBuilder = AuthNewResponse.builder()
        .success(true)
        .request(event);

    try {
      AuthSession created = client.create(authSession);
      logger.log(String.format("persisted auth_session %s", created));
    } catch (RuntimeException e) {
      String msg = String.format("error in saving auth_session for %s: %s", authSession.getIdentity(), e.getMessage());
      logger.log(msg);
      return responseBuilder
          .success(false)
          .message(msg)
          .build();
    }

    try {
      if (IdentityType.EMAIL == IdentityType.withValue(event.getIdentityType())) {
        String body = String.format("<a href=\"https://auth-me.com/verify?token=%s\">Authorize Me</a>", token);
        mailConfig.send(event.getIdentity(), "Auth Me Request", body);
        logger.log(String.format("sent email auth_session for %s", authSession.getIdentity()));
      } else {
        logger.log(String.format("identity type %s not supported; email or phone only", event.getIdentityType()));
      }
    } catch (RuntimeException e) {
      String msg = String.format("error sending email for %s: %s", authSession.getIdentity(), e.getMessage());
      logger.log(msg);
      return responseBuilder
          .success(false)
          .message(msg)
          .build();
    }

    return responseBuilder.build();
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
