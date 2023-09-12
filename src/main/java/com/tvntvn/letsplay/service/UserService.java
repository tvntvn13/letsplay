package com.tvntvn.letsplay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.model.UserUpdateRequest;
import com.tvntvn.letsplay.repository.ProductRepository;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.util.EmailValidatorService;
import com.tvntvn.letsplay.util.InputSanitizer;
import com.tvntvn.letsplay.util.ResponseFormatter;

@Service
public class UserService implements UserDetailsService {
  private UserRepository repository;

  private InputSanitizer input;

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  private JwtService jwtService;

  private ResponseFormatter formatter;

  @Autowired private ProductRepository productRepository;

  @Autowired private EmailValidatorService validator;

  @Autowired
  public void setRepository(UserRepository repository) {
    this.repository = repository;
  }

  @Autowired
  public void setInput(InputSanitizer inputSanitizer) {
    this.input = inputSanitizer;
  }

  @Autowired
  public void setJwtService(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Autowired
  public void setFormatter(ResponseFormatter formatter) {
    this.formatter = formatter;
  }

  private Boolean isValidEmail(String email) {
    return validator.validate(email);
  }

  private Boolean isValidPassword(String password) {
    if (password == null) return false;
    if (password.length() < 5 || password.length() > 50) {
      return false;
    }
    return password.matches("\\A\\p{ASCII}*\\z");
  }

  public ResponseEntity<Object> addAdminRights(String name) {
    String clean = input.sanitize(name);
    User existingUser = repository.findByName(clean).orElse(null);
    if (existingUser != null) {
      existingUser.setRole("user,admin");
      repository.save(existingUser);
      return formatter.format(
          "admin rights granted to user:", existingUser.getName(), HttpStatus.OK);
    } else {
      return formatter.format("user not found with name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> removeAdminRights(String name, String token) {
    String clean = input.sanitize(name);
    String reqUsername = jwtService.extractUsername(token);
    User user = repository.findByName(reqUsername).orElse(null);
    if (clean.equals("admin") && reqUsername.equals("admin")) {
      return formatter.format("please, dont do that", HttpStatus.I_AM_A_TEAPOT);
    } else if (clean.equals("admin") && !reqUsername.equals("admin")) {
      return deleteUser(user.getName(), token);
    }

    User existingUser = repository.findByName(clean).orElse(null);
    if (existingUser != null) {
      existingUser.setRole("user");
      repository.save(existingUser);
      return formatter.format(
          "admin rights removed from user:", existingUser.getName(), HttpStatus.OK);
    } else {
      return formatter.format("user not found with name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> findAllUsers() {
    return formatter.formatUserList(repository.findAll(), HttpStatus.OK);
  }

  public ResponseEntity<Object> findUserById(String id) {
    String clean = input.sanitize(id);
    User user = repository.findById(clean).orElse(null);
    if (user != null) {
      return formatter.format(user, HttpStatus.OK);
    } else {
      return formatter.format("user not found with id:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> findUserByName(String name) {
    String clean = input.sanitize(name);
    User user = repository.findByName(clean).orElse(null);
    if (user != null) {
      return formatter.format(user, HttpStatus.OK);
    } else {
      return formatter.format("user not found by name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> findUserByEmail(String email) {
    String clean = input.sanitize(email);
    User user = repository.findByEmail(clean).orElse(null);
    if (user != null) {
      return formatter.format(user, HttpStatus.OK);
    } else {
      return formatter.format("user not found by email: ", clean, HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> updateUser(UserUpdateRequest user, String token) {
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

    String updateName = user.getName() == null ? "" : user.getName().get();
    String updateEmail = user.getEmail() == null ? "" : user.getEmail().get();
    String updatePassword = user.getPassword() == null ? "" : user.getPassword().get();

    String oldPassword = existingUser == null ? "" : existingUser.getPassword();
    String oldName = existingUser == null ? "" : existingUser.getName();

    if (!updateName.equals("") && !input.sanitize(updateName).equals("")) {
      if (!repository.findByName(updateName).isPresent()) {
        existingUser.setName(input.sanitize(updateName));
      } else {
        return formatter.format("name already taken: " + updateName, HttpStatus.CONFLICT);
      }
    }
    if (!updatePassword.equals("") && isValidPassword(updatePassword)) {
      existingUser.setPassword(encoder.encode(input.sanitize(updatePassword)));
    }
    if (!updateEmail.equals("") && isValidEmail(updateEmail)) {
      if (!repository.findByEmail(updateEmail).isPresent()) {
        existingUser.setEmail(updateEmail);
      } else {
        return formatter.format("email already taken: " + updateEmail, HttpStatus.CONFLICT);
      }
    } else if (!updateEmail.equals("") && !isValidEmail(updateEmail)) {
      return formatter.format("email invalid: " + updateEmail, HttpStatus.BAD_REQUEST);
    }
    try {
      repository.save(existingUser);
      if (oldPassword.equals(existingUser.getPassword())
          && oldName.equals(existingUser.getName())) {
        return formatter.format(existingUser, HttpStatus.OK);
      } else {
        return formatter.format(
            "user updated: " + existingUser.getName() + ". token invalidated, please login again",
            HttpStatus.OK);
      }

    } catch (Exception e) {
      return formatter.format("could not update user", HttpStatus.BAD_REQUEST);
    }
  }

  public ResponseEntity<Object> deleteUser(String name, String token) {
    String clean = input.sanitize(name);
    User user = repository.findByName(clean).orElse(null);
    if (name.equals("admin")) {
      String username = jwtService.extractUsername(token);
      return formatter.format("please, stop " + username, HttpStatus.I_AM_A_TEAPOT);
    }
    if (user != null) {
      repository.deleteByName(clean);
      productRepository.deleteAllByUserId(user.getId());
      return formatter.format("user and all products deleted by name:", clean, HttpStatus.OK);
    } else {
      return formatter.format("user not found by name:", clean, HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = repository.findByName(username);
    if (user.isPresent()) return user.get();
    throw new UsernameNotFoundException("user not found");
  }
}
