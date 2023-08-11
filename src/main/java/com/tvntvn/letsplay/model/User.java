package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
  @Id private String id;
  @NotBlank private String name;
  @NotBlank @Email private String email;
  @NotBlank private String password;
  @NotBlank private String role;

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }
}
