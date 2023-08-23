package com.tvntvn.letsplay.controller;

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
import com.tvntvn.letsplay.util.ResponseFormatter;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired PasswordEncoder encoder;

  @Autowired private InputSanitizer s;

  @Autowired private ResponseFormatter formatter;

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
      return formatter.format("user already exists", HttpStatus.CONFLICT);
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

  // @GetMapping("/logout")
  // @PreAuthorize("hasAuthority('user')")
  // public ResponseEntity<Object> logoutAndExpireToken(
  //     @RequestHeader("Authorization") String header) {
  //   System.out.println("i got to here...");
  //   String token = header.substring(7);
  //   String username = jwtService.extractUsername(token);
  //   User user = userRepository.findByName(username).get();
  //   user.setCredendtialsNotExpired(false);
  // try {
  //   System.out.println("inside the try block");
  //   Authentication authentication =
  //       authenticationManager.authenticate(
  //           new UsernamePasswordAuthenticationToken(username, password));
  //   System.out.println("after authentication");
  //   if (!authentication.isAuthenticated()) {
  //     System.out.println("setting the authenticated to false now");
  //     authentication.setAuthenticated(false);
  //   }
  //   // return formatter.format("geez", HttpStatus.I_AM_A_TEAPOT);
  // } catch (Exception e) {
  //   formatter.format("just catched this " + e.getLocalizedMessage(), HttpStatus.IM_USED);
  // }
  // String newToken = jwtService.revokeToken(username);
  // return formatter.format("You are now logged out, " + username, HttpStatus.OK);
  // }
}
