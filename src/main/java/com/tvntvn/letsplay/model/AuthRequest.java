package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class AuthRequest {
  @Getter
  @NotEmpty(message = "username cannot be empty")
  @Size(min = 3, max = 30, message = "username or password is wrong")
  private String name;

  @Getter
  @NotEmpty(message = "password cannot be empty")
  @Size(min = 4, max = 50, message = "username or password is wrong")
  private String password;
}
