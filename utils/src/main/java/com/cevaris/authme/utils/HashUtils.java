package com.cevaris.authme.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashUtils {

  public static String toString(String x) {
    HashFunction hf = Hashing.murmur3_128();
    hf.hashString(x, Charset.defaultCharset());
    return hf.toString();
  }

  public static String timedHash(String x) {
    return Hashing.murmur3_128()
        .newHasher()
        .putString(x, Charset.defaultCharset())
        .putLong(DateTimeUtils.now().getMillis())
        .hash()
        .toString();
  }

}
