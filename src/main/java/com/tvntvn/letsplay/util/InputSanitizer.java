package com.tvntvn.letsplay.util;

public class InputSanitizer {

  public static String sanitize(String s) {

    String sanitized = s.replaceAll("[^\\w\\s-]", "");
    return sanitized;
  }
}
