package com.tvntvn.letsplay.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.tvntvn.letsplay.model.AuthRequest;
import com.tvntvn.letsplay.model.SignupRequest;
import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.service.JwtService;
import com.tvntvn.letsplay.util.InputSanitizer;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired PasswordEncoder encoder;

  @Autowired private InputSanitizer s;

  @Autowired private UserRepository userRepository;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtService jwtService;

  @PostMapping("/signup")
  public ResponseEntity<Object> addNewUser(@RequestBody @Valid SignupRequest user) {

    System.out.println(user.toString());
    String cleanName = s.sanitize(user.getName());
    String cleanEmail = s.sanitize(user.getEmail());
    String cleanPassword = s.sanitize(user.getPassword());

    if (userRepository.findByName(cleanName).isPresent()) {
      return new ResponseEntity<Object>("user already exists", HttpStatus.CONFLICT);
    }
    if (userRepository.findByEmail(cleanEmail).isPresent()) {
      return new ResponseEntity<Object>("email already taken", HttpStatus.CONFLICT);
    }
    User newUser = new User(cleanName, cleanEmail, encoder.encode(cleanPassword));
    newUser.setRole("user");
    userRepository.save(newUser);
    return new ResponseEntity<Object>(
        "user: " + cleanName + " registered succesfully", HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<Object> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    String cleanName = s.sanitize(authRequest.getName());
    String cleanPassword = s.sanitize(authRequest.getPassword());
    Map<String, String> response = new HashMap<>();
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(cleanName, cleanPassword));
      System.out.println(authentication.toString());
      if (authentication.isAuthenticated()) {
        System.out.println("authenticated!");
        response.put("status", "succesfully logged in");
        response.put("payload", jwtService.generateToken(cleanName));
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        System.out.println("something went wrong");
        return new ResponseEntity<Object>("invalid username or password", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }
  }
}
