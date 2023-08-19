package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthRequest {
  @Setter
  @Getter
  @NotEmpty(message = "username cannot be empty")
  private String name;

  @Setter
  @Getter
  @NotEmpty(message = "password cannot be empty")
  private String password;
}
