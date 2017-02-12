package com.cevaris.authme.utils.mail;

public interface Mailer {

  void send(String to, String subject, String text) throws RuntimeException;

}
