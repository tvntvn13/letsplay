package com.tvntvn.letsplay.util;

import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

  public InputSanitizer() {}

  public String sanitize(String s) {
    String sanitized = s.replaceAll("[^\\w -@!.]", "").trim();
    return sanitized;
  }
}
