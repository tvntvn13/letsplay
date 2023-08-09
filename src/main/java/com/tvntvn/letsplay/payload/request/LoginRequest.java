package com.tvntvn.letsplay.payload.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
  @NotEmpty private String username;

  @NotEmpty private String password;
}
