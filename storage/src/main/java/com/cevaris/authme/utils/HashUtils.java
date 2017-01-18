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

}
