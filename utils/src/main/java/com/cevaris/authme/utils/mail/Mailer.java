package com.cevaris.authme.utils.mail;

public interface Mailer {

  Boolean send(String to, String subject, String text);

}
