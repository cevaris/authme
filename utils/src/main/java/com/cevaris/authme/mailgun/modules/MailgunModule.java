package com.cevaris.authme.mailgun.modules;

import com.cevaris.authme.utils.PropertyStore;
import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.sargue.mailgun.Configuration;

public class MailgunModule implements Module {

  // https://github.com/sargue/mailgun
  // https://documentation.mailgun.com/quickstart-sending.html

  public static final String EMAIL_API_KEY = "email.api.key";
  public static final String EMAIL_DOMAIN = "email.domain";
  public static final String EMAIL_FROM = "email.from";
  public static final String EMAIL_FROM_NAME = "email.from.name";

  @Provides
  @Singleton
  Configuration provideConfiguration(PropertyStore propertyStore) {
    String key = propertyStore.get(EMAIL_API_KEY);
    String domain = propertyStore.get(EMAIL_DOMAIN);
    String from = propertyStore.get(EMAIL_FROM);
    String fromName = propertyStore.get(EMAIL_FROM_NAME);

    Preconditions.checkNotNull(key, domain, from, fromName);

    return new Configuration()
        .domain(domain)
        .apiKey(key)
        .from(fromName, from);
  }

  public void configure(Binder binder) {
  }

}
