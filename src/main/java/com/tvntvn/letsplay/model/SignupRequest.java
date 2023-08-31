package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Getter;

@ValidateOnExecution
public class SignupRequest {

  @Getter
  @Size(min = 3, max = 30, message = "name has to be between 3-30 characters")
  @NotBlank(message = "name cannot be empty")
  private String name;

  @Getter
  @Email(message = "email has to be valid")
  private String email;

  @Getter
  @Size(min = 4, max = 50, message = "password has to be between 5-50 characters")
  @NotBlank(message = "password cannot be empty")
  private String password;
}
