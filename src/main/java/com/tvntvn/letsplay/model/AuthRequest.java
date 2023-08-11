package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthRequest {
  @Setter @Getter @NotEmpty private String username;
  @Setter @Getter @NotEmpty private String password;
}
