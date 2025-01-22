package com.tvntvn.letsplay.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "users")
@NoArgsConstructor
public class User implements UserDetails {
  @JsonIgnore @Id private String id;

  @Field private String name;

  @Field private String email;

  @Field @JsonIgnore private String password;

  @Field private String role;

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public User(String name, String email, String password, String role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    String[] split = getRole().split(",");

    for (String auth : split) {
      authorities.add(new SimpleGrantedAuthority(auth));
    }
    return authorities;
  }

  @JsonIgnore
  public List<SimpleGrantedAuthority> getRoles() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    String[] split = getRole().split(",");

    for (String auth : split) {
      authorities.add(new SimpleGrantedAuthority(auth));
    }
    return authorities;
  }

  @Override
  @JsonIgnore
  public String getUsername() {
    return this.name;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }
}
