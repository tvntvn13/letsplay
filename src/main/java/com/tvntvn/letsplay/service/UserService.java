package com.tvntvn.letsplay.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.util.InputSanitizer;
import com.tvntvn.letsplay.util.ResponseFormatter;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {
  private UserRepository repository;
  private InputSanitizer s;
  private JwtService jwtService;
  private ResponseFormatter formatter;

  @Value("${adminName}")
  private String adminName;

  @Value("${adminPassword}")
  private String adminPassword;

  @Value("${adminEmail}")
  private String adminEmail;

  @Value("${adminRoles}")
  private String adminRoles;

  @Autowired
  public void setRepository(UserRepository repository) {
    this.repository = repository;
  }

  @Autowired
  public void setS(InputSanitizer s) {
    this.s = s;
  }

  @Autowired
  private PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public void setJwtService(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  public void setFormatter(ResponseFormatter formatter) {
    this.formatter = formatter;
  }

  public ResponseEntity<Object> addAdminRights(String name) {
    String clean = s.sanitize(name);
    User existingUser = repository.findByName(clean).orElse(null);
    if (existingUser != null) {
      existingUser.setRole("user,admin");
      return formatter.format(
          "admin rights granted to user:", existingUser.getName(), HttpStatus.OK);
    } else {
      return formatter.format("user not found with id:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> removeAdminRights(String name) {
    String clean = s.sanitize(name);
    User existingUser = repository.findByName(clean).orElse(null);
    if (existingUser != null) {
      existingUser.setRole("user");
      return formatter.format(
          "admin rights removed from user:", existingUser.getName(), HttpStatus.OK);
    } else {
      return formatter.format("user not found with name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<List<User>> findAllUsers() {
    return new ResponseEntity<List<User>>(repository.findAll(), HttpStatus.OK);
  }

  public ResponseEntity<Object> findUserById(String id) {
    String clean = s.sanitize(id);
    User user = repository.findById(clean).orElse(null);
    if (user != null) {
      return formatter.format(user, HttpStatus.OK);
    } else {
      return formatter.format("user not found with id:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> findUserByName(String name) {
    String clean = s.sanitize(name);
    User user = repository.findByName(clean).orElse(null);
    if (user != null) {
      return formatter.format(user, HttpStatus.OK);
    } else {
      return formatter.format("user not found by name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> findUserByEmail(String email) {
    String clean = s.sanitize(email);
    User user = repository.findByEmail(clean).orElse(null);
    if (user != null) {
      return formatter.format(user, HttpStatus.OK);
    } else {
      return formatter.format("user not found by email:", clean, HttpStatus.NOT_FOUND);
    }
  }

  // TODO ADD EMAIL VALIDATION
  public ResponseEntity<Object> updateUser(User user, String token) {
    String username = jwtService.extractUsername(token);
    User existingUser = repository.findByName(username).orElse(null);
    String id = existingUser != null ? existingUser.getId() : null;
    if (id == null || existingUser == null)
      return formatter.format("bad request", HttpStatus.BAD_REQUEST);
    if (repository.findById(id).isPresent()) {
      existingUser = repository.findById(id).get();
    } else {
      return formatter.format("something went wrong, try again", HttpStatus.CONFLICT);
    }

    if (user.getName() != null) {
      existingUser.setName(s.sanitize(user.getName()));
    }
    if (user.getPassword() != null) {
      existingUser.setPassword(encoder().encode(s.sanitize(user.getPassword())));
    }
    if (user.getEmail() != null) {
      existingUser.setEmail(s.sanitize(user.getEmail()));
    }
    try {
      repository.save(existingUser);
      return formatter.format("user " + username + " updated", HttpStatus.OK);
    } catch (Exception e) {
      return formatter.format("could nout update user", HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity<Object> deleteUser(String name) {
    String clean = s.sanitize(name);
    User user = repository.findByName(clean).orElse(null);
    if (user != null) {
      repository.deleteById(clean);
      return formatter.format("user deleted by id:", clean, HttpStatus.OK);
    } else {
      return formatter.format("user not found by id:", clean, HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = repository.findByName(username);
    if (user.isPresent()) return user.get();
    throw new UsernameNotFoundException("user not found");
  }

  @PostConstruct
  private void initAdmin() {
    if (repository.findByName("admin").isEmpty()) {
      User admin = new User("admin", "admin@admin.com", encoder().encode("root"), "user,admin");
      repository.save(admin);
    }
  }
}
