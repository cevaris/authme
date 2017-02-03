package com.cevaris.authme.utils.mail;


import com.google.inject.Inject;

import java.util.logging.Logger;

public class LoggerMailer implements Mailer {

  private Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  public LoggerMailer() {
  }

  public Boolean send(String to, String subject, String text) {
    logger.info(String.format("to=[%s] subject=[%s] text[%s]", to, subject, text));
    return true;
  }
}
