package com.cevaris.authme.utils.mail;


import java.util.logging.Logger;

import com.google.inject.Inject;

public class LoggerMailer implements Mailer {

  private Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  public LoggerMailer() {
  }

  public void send(String to, String subject, String text) throws RuntimeException {
    logger.info(String.format("to=[%s] subject=[%s] text[%s]", to, subject, text));
  }
}
