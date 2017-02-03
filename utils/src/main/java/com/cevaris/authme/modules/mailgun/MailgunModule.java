package com.cevaris.authme.modules.mailgun;

import com.cevaris.authme.utils.mail.Mailer;
import com.cevaris.authme.utils.mail.MailgunMailer;
import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.sargue.mailgun.Configuration;

public class MailgunModule implements Module {

  // https://github.com/sargue/mailgun
  // https://documentation.mailgun.com/quickstart-sending.html

  public static final String EMAIL_API_KEY = "EMAIL_API_KEY";
  public static final String EMAIL_DOMAIN = "EMAIL_DOMAIN";

  public static String getEmailApiKey() {
    return EMAIL_API_KEY;
  }

  public static final String EMAIL_FROM = "EMAIL_FROM";

  @Provides
  @Singleton
  Configuration provideConfiguration() {
    String key = System.getenv(EMAIL_API_KEY);
    String domain = System.getenv(EMAIL_DOMAIN);
    String from = System.getenv(EMAIL_FROM);

    Preconditions.checkNotNull(key, EMAIL_API_KEY + " env variable not set");
    Preconditions.checkNotNull(domain, EMAIL_DOMAIN + " env variable not set");

    return new Configuration()
        .domain(domain)
        .apiKey(key)
        .from("Auth Me", from);
  }

  public void configure(Binder binder) {
    binder.bind(Mailer.class).to(MailgunMailer.class);
  }
}
