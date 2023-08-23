package com.tvntvn.letsplay.service;

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
import com.tvntvn.letsplay.repository.ProductRepository;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.util.EmailValidatorService;
import com.tvntvn.letsplay.util.InputSanitizer;
import com.tvntvn.letsplay.util.ResponseFormatter;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {
  private UserRepository repository;

  private InputSanitizer input;

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  private JwtService jwtService;

  private ResponseFormatter formatter;

  @Autowired private ProductRepository productRepository;

  @Autowired private EmailValidatorService validator;

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
    if (reqUsername.equals("admin")) {
      return formatter.format("please, dont do that", HttpStatus.I_AM_A_TEAPOT);
    } else if (clean.equals("admin") && !user.getName().equals("admin")) {
      return deleteUser(user.getName());
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
    // return new ResponseEntity<List<Object>>(repository.findAll(), HttpStatus.OK);
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

    if (user.getName() != null && input.sanitize(user.getName()) != "") {
      existingUser.setName(input.sanitize(user.getName()));
    }
    if (user.getPassword() != null && isValidPassword(user.getPassword())) {
      existingUser.setPassword(encoder.encode(input.sanitize(user.getPassword())));
    }
    if (user.getEmail() != null) {
      System.out.println(user.getEmail());
      System.out.println(isValidEmail(user.getEmail()));
      if (isValidEmail(user.getEmail())) {
        existingUser.setEmail(user.getEmail());
      } else {
        return formatter.format("email invalid: " + user.getEmail(), HttpStatus.NOT_ACCEPTABLE);
      }
    }
    try {
      repository.save(existingUser);
      return formatter.format(existingUser, HttpStatus.OK);
    } catch (Exception e) {
      return formatter.format("could nout update user", HttpStatus.BAD_REQUEST);
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

  @PostConstruct
  private void initAdmin() {
    if (repository.findByName("admin").isEmpty()) {
      User admin = new User("admin", "admin@admin.com", encoder.encode("root"), "user,admin");
      repository.save(admin);
    }
  }
}
