package com.tvntvn.letsplay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
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
import com.tvntvn.letsplay.util.EmailValidatorService;
import com.tvntvn.letsplay.util.InputSanitizer;
import com.tvntvn.letsplay.util.ResponseFormatter;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

  @Autowired PasswordEncoder encoder;

  @Autowired private InputSanitizer s;

  @Autowired private ResponseFormatter formatter;

  @Autowired private UserRepository userRepository;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtService jwtService;

  @Autowired private EmailValidatorService emailValidator;

  @PostMapping("/signup")
  public ResponseEntity<Object> addNewUser(@Valid @RequestBody SignupRequest user) {

    String cleanName = s.sanitize(user.getName());
    String cleanEmail = s.sanitize(user.getEmail());
    String cleanPassword = s.sanitize(user.getPassword());

    if (userRepository.findByName(cleanName).isPresent()) {
      return formatter.format("user already exists", HttpStatus.CONFLICT);
    }
    if (!emailValidator.validate(cleanEmail)) {
      return formatter.format("invalid email", HttpStatus.BAD_REQUEST);
    }
    if (userRepository.findByEmail(cleanEmail).isPresent()) {
      return formatter.format("email already taken", HttpStatus.CONFLICT);
    }
    User newUser = new User(cleanName, cleanEmail, encoder.encode(cleanPassword));
    newUser.setRole("user");
    userRepository.save(newUser);
    return formatter.format("user " + cleanName + " registered succesfully", HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<Object> authenticateAndGetToken(
      @RequestBody @Valid AuthRequest authRequest) {

    String cleanName = s.sanitize(authRequest.getName());
    String cleanPassword = s.sanitize(authRequest.getPassword());

    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(cleanName, cleanPassword));
      if (authentication.isAuthenticated()) {
        return formatter.format(jwtService.generateToken(cleanName), HttpStatus.OK);
      } else {
        return formatter.format("invalid username or password", HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return formatter.format(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }
  }
}
