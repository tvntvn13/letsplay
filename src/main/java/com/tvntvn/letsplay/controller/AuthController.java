package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.AuthRequest;
import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.service.JwtService;
import com.tvntvn.letsplay.service.UserService;
import com.tvntvn.letsplay.util.InputSanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired PasswordEncoder encoder;

  // @Autowired private UserService userService;
  @Autowired private UserService userService;

  @Autowired private UserRepository userRepository;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtService jwtService;

  @PostMapping("/signup")
  public ResponseEntity<String> addNewUser(@RequestBody User user) {
    String cleanName = InputSanitizer.sanitize(user.getName());
    String cleanEmail = InputSanitizer.sanitize(user.getEmail());
    String cleanPassword = InputSanitizer.sanitize(user.getPassword());

    if (userService.findUserByName(cleanName).isPresent()) {
      return new ResponseEntity<String>("user already exists", HttpStatus.CONFLICT);
    }
    if (userService.findUserByEmail(cleanEmail).isPresent()) {
      return new ResponseEntity<String>("email already taken", HttpStatus.CONFLICT);
    }
    User newUser = new User(cleanName, cleanEmail, encoder.encode(cleanPassword));
    newUser.setRole("user");
    userRepository.save(newUser);
    return new ResponseEntity<String>(
        "user: " + cleanName + " registered succesfully", HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    String cleanName = InputSanitizer.sanitize(authRequest.getUsername());
    String cleanPassword = InputSanitizer.sanitize(authRequest.getPassword());
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(cleanName, cleanPassword));
      System.out.println(authentication.toString());
      if (authentication.isAuthenticated()) {
        System.out.println("authenticated!");
        return new ResponseEntity<>(
            jwtService.generateToken(authRequest.getUsername()), HttpStatus.OK);
      } else {
        System.out.println("something went wrong");
        return new ResponseEntity<String>("invalid username or password", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<String>(
          "bad request, pls try again, " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
