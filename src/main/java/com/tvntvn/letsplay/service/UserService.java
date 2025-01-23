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
    final String ADMIN = "admin";
    if (clean.equals(ADMIN) && reqUsername.equals(ADMIN)) {
      return formatter.format("please, dont do that", HttpStatus.I_AM_A_TEAPOT);
    } else if (clean.equals(ADMIN) && !reqUsername.equals(ADMIN)) {
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
    User existingUser = findExistingUser(token);
    if (existingUser == null) {
      return formatter.format("bad request", HttpStatus.BAD_REQUEST);
    }

    UpdateResult result = validateAndUpdateUser(existingUser, user);
    return processUpdateResult(existingUser, result);
  }

  private User findExistingUser(String token) {
    String username = jwtService.extractUsername(token);
    return repository.findByName(username).orElse(null);
  }

  private record UpdateResult(boolean nameUpdated, boolean passwordUpdated, boolean emailUpdated) {}

  private UpdateResult validateAndUpdateUser(User existingUser, UserUpdateRequest user) {
    boolean nameUpdated = updateName(existingUser, user);
    boolean passwordUpdated = updatePassword(existingUser, user);
    boolean emailUpdated = updateEmail(existingUser, user);
    return new UpdateResult(nameUpdated, passwordUpdated, emailUpdated);
  }

  private boolean updateName(User existingUser, UserUpdateRequest user) {
    String updateName = user.getName() == null ? "" : user.getName().get();
    if (updateName.isEmpty() || !input.sanitize(updateName).isEmpty()) return false;

    if (repository.findByName(updateName).isPresent()) {
      throw new UserUpdateException("name already taken: " + updateName);
    }

    existingUser.setName(input.sanitize(updateName));
    return true;
  }

  private boolean updatePassword(User existingUser, UserUpdateRequest user) {
    String updatePassword = user.getPassword() == null ? "" : user.getPassword().get();
    if (updatePassword.isEmpty() || Boolean.TRUE.equals(!isValidPassword(updatePassword)))
      return false;

    existingUser.setPassword(encoder.encode(input.sanitize(updatePassword)));
    return true;
  }

  private boolean updateEmail(User existingUser, UserUpdateRequest user) {
    String updateEmail = user.getEmail() == null ? "" : user.getEmail().get();
    if (updateEmail.isEmpty()) return false;

    if (Boolean.FALSE.equals(isValidEmail(updateEmail))) {
      throw new UserUpdateException("email invalid: " + updateEmail);
    }

    if (repository.findByEmail(updateEmail).isPresent()) {
      throw new UserUpdateException("email already taken: " + updateEmail);
    }

    existingUser.setEmail(updateEmail);
    return true;
  }

  private ResponseEntity<Object> processUpdateResult(User existingUser, UpdateResult result) {
    try {
      repository.save(existingUser);

      if (result.nameUpdated() || result.passwordUpdated()) {
        return formatter.format(
            "user updated: " + existingUser.getName() + ". token invalidated, please login again",
            HttpStatus.OK);
      }

      return formatter.format(existingUser, HttpStatus.OK);
    } catch (Exception e) {
      return formatter.format("could not update user", HttpStatus.BAD_REQUEST);
    }
  }

  private class UserUpdateException extends RuntimeException {
    public UserUpdateException(String message) {
      super(message);
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
