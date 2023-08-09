package com.tvntvn.letsplay.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class SignupRequest {
  @NotEmpty
  @Size(min = 3, max = 40)
  @Getter
  @Setter
  private String username;

  @NotEmpty
  @Size(max = 70)
  @Email
  @Getter
  @Setter
  private String email;

  @NotEmpty
  @Size(min = 3, max = 50)
  @Getter
  @Setter
  private String password;

  @Getter @Setter private Set<String> roles;
}
