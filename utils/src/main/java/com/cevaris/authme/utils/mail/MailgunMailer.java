package com.cevaris.authme.utils.mail;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;

public class MailgunMailer implements Mailer {

  private Configuration config;

  private Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  public MailgunMailer(Configuration config) {
    this.config = config;
  }

  public Boolean send(String to, String subject, String text) {
    Preconditions.checkNotNull(to, subject, text);

    logger.info(String.format("to=[%s] subject=[%s] text[%s]", to, subject, text));

    Response r = Mail.using(config)
        .to(to)
        .subject(subject)
        .text(text)
        .build()
        .send();

    if (!r.isOk()) {
      logger.log(Level.SEVERE, String.format("error sending message to %s: %s", to, r.responseMessage()));
    }

    return r.isOk();
  }
}
