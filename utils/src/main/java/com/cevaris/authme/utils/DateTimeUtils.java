package com.cevaris.authme.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeUtils extends org.joda.time.DateTimeUtils {

  public static DateTime now() {
    return org.joda.time.DateTime.now().withZone(DateTimeZone.UTC);
  }

  public static void setCurrentMillisFixed(DateTime dateTime) {
    setCurrentMillisFixed(dateTime.getMillis());
  }

}
