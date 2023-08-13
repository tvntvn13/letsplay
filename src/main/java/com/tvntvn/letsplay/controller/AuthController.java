package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.AuthRequest;
import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.service.JwtService;
import com.tvntvn.letsplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  @Autowired private UserService userService;

  @Autowired private UserRepository userRepository;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtService jwtService;

  @PostMapping("/signup")
  public String addNewUser(@RequestBody User user) {
    if (userService.findUserByName(user.getName()).isPresent()) {
      return "user already exists";
    }
    if (userService.findUserByEmail(user.getEmail()).isPresent()) {
      return "email is already taken";
    }
    User newUser = new User(user.getName(), user.getEmail(), encoder.encode(user.getPassword()));
    newUser.setRole("user");
    userRepository.save(newUser);
    return "user " + user.getName() + " registered";

    // try {
    //   userService.addUser(user);
    // } catch (Exception e) {
    //   return "error: " + e.getMessage();
    // }
    // return "user " + user.getName() + " was created";
  }

  @PostMapping("/login")
  public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    System.out.println("in the login now.");
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  authRequest.getUsername(), authRequest.getPassword()));
      System.out.println(authentication.toString());
      if (authentication.isAuthenticated()) {
        System.out.println("authenticated!");
        return jwtService.generateToken(authRequest.getUsername());
      } else {
        System.out.println("something went wrong");
        throw new UsernameNotFoundException("invalid user request");
      }
    } catch (Exception e) {
      System.out.println("catched error: " + e.getMessage());
    }
    return "couldn't authenticate";
  }
}
