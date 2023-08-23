package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

  @Size(min = 3, max = 30, message = "name has to be between 3-30 characters")
  @NotBlank(message = "name cannot be empty")
  private String name;

  @Email(message = "email has to be valid")
  private String email;

  @Size(min = 4, max = 50, message = "password has to be between 5-50 characters")
  @NotBlank(message = "password cannot be empty")
  private String password;
}
