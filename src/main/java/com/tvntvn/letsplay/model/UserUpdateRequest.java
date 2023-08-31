package com.tvntvn.letsplay.model;

import java.util.Optional;

import com.mongodb.lang.Nullable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
  @Size(min = 3, max = 30, message = "name is not valid")
  @Nullable
  private Optional<String> name;

  @Size(min = 4, max = 50, message = "password  is not valid")
  @Nullable
  private Optional<String> password;

  @Email(message = "email is not valid")
  private Optional<String> email;
}
