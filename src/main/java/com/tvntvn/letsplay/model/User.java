package com.tvntvn.letsplay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Data
@AllArgsConstructor
public class User {
  @Id private String id;
  private String name;
  private String email;
  private String password;
  private String role;
}
