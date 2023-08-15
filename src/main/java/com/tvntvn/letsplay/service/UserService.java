package com.tvntvn.letsplay.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.util.InputSanitizer;
import com.tvntvn.letsplay.util.ResponseFormatter;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {

  @Autowired private UserRepository repository;

  @Autowired private InputSanitizer s;

  @Autowired private PasswordEncoder encoder;

  @Autowired private JwtService jwtService;

  @Autowired private ResponseFormatter formatter;

  public ResponseEntity<Object> addAdminRights(String name) {
    String clean = s.sanitize(name);
    if (repository.findByName(clean).isPresent()) {
      User existingUser = repository.findByName(clean).get();
      existingUser.setRole("user,admin");
      return formatter.format(
          "admin rights granted to user:", existingUser.getName(), HttpStatus.OK);
    } else {
      return formatter.format("user not found with id:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> removeAdminRights(String name) {
    String clean = s.sanitize(name);
    if (repository.findByName(clean).isPresent()) {
      User existingUser = repository.findByName(clean).get();
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
    if (repository.findById(clean).isPresent()) {
      return formatter.format(repository.findById(clean).get(), HttpStatus.OK);
    } else {
      return formatter.format("user not found with id:", clean, HttpStatus.NO_CONTENT);
    }
  }

  public ResponseEntity<Object> findUserByName(String name) {
    String clean = s.sanitize(name);
    if (repository.findByName(clean).isPresent()) {
      return formatter.format(repository.findByName(clean).get(), HttpStatus.OK);
    } else {
      return formatter.format("user not found by name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> findUserByEmail(String email) {
    String clean = s.sanitize(email);
    if (repository.findByEmail(clean).isPresent()) {
      return formatter.format(repository.findByEmail(clean).get(), HttpStatus.OK);
    } else {
      return formatter.format("user not found by email:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> updateUser(User user, String token) {
    String username = jwtService.extractUsername(token);
    String id = repository.findByName(username).get().getId();
    User existingUser = repository.findByName(username).get();
    if (repository.findById(id).isPresent()) {
      existingUser = repository.findById(id).get();
    } else {
      return formatter.format("something went wrong, try again", HttpStatus.CONFLICT);
    }

    if (user.getName() != null) {
      existingUser.setName(s.sanitize(user.getName()));
    }
    if (user.getPassword() != null) {
      existingUser.setPassword(encoder.encode(s.sanitize(user.getPassword())));
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

  public ResponseEntity<Object> deleteUser(String id) {
    String clean = s.sanitize(id);
    if (repository.findById(clean).isPresent()) {
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
      User admin = new User("admin", "admin@admin.com", encoder.encode("root"), "user,admin");
      repository.save(admin);
    }
  }
}
