package com.tvntvn.letsplay.model;

import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
  @Id private String id;

  @NonNull
  @Size(max = 40)
  private String name;

  @NonNull
  @Email
  @Size(max = 70)
  private String email;

  @NonNull
  @Size(max = 60)
  private String password;

  @DBRef private Set<Role> roles = new HashSet<>();

  public User() {}

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }
}
