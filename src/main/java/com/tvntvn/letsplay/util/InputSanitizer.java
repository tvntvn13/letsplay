package com.tvntvn.letsplay.util;

import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

  public String sanitize(String s) {
    if (s == null) return null;
    return s.replaceAll("[^\\w @!.-]", "").trim();
  }
}
