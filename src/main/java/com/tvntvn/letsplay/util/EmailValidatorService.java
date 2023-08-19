package com.tvntvn.letsplay.util;

import org.springframework.stereotype.Service;

@Service
public class EmailValidatorService {
  public Boolean validate(String email) {
    if (email == null) return false;
    String regex =
        "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[A-Z0-9-]+\\.)+[A-Z]{2,6}$";

    return email.matches(regex);
  }
}
