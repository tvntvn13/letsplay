package com.tvntvn.letsplay.util;

import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

  public InputSanitizer() {}

  public String sanitize(String s) {
    if (s == null) return null;
    String sanitized = s.replaceAll("[^\\w -@!.]", "").trim();
    return sanitized;
  }
}
