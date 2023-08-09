package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.Role;
import com.tvntvn.letsplay.model.RoleEnum;
import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.payload.request.LoginRequest;
import com.tvntvn.letsplay.payload.request.SignupRequest;
import com.tvntvn.letsplay.payload.response.MessageResponse;
import com.tvntvn.letsplay.payload.response.UserInfoResponse;
import com.tvntvn.letsplay.repository.RoleRepository;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.security.jwt.JwtUtils;
import com.tvntvn.letsplay.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired AuthenticationManager authenticationManager;

  @Autowired UserRepository userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired PasswordEncoder encoder;

  @Autowired JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(
            new UserInfoResponse(
                userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
    if (userRepository.existsByName(signupRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(
              new MessageResponse(
                  "error: username " + signupRequest.getUsername() + " is already taken"));
    }
    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(
              new MessageResponse(
                  "error: email " + signupRequest.getEmail() + " is already taken"));
    }

    User user =
        new User(
            signupRequest.getUsername(),
            signupRequest.getEmail(),
            encoder.encode(signupRequest.getPassword()));

    Set<String> stringRoles = signupRequest.getRoles();
    Set<Role> roles = new HashSet<>();

    if (stringRoles == null) {
      Role userRole =
          roleRepository
              .findByName(RoleEnum.user)
              .orElseThrow(() -> new RuntimeException("error: role not found"));
      roles.add(userRole);
    } else {
      stringRoles.forEach(
          role -> {
            switch (role) {
              case "admin":
                Role adminRole =
                    roleRepository
                        .findByName(RoleEnum.admin)
                        .orElseThrow(() -> new RuntimeException("error: role not found"));
                roles.add(adminRole);
                break;
              default:
                Role userRole =
                    roleRepository
                        .findByName(RoleEnum.user)
                        .orElseThrow(() -> new RuntimeException("error: role not found"));
                roles.add(userRole);
            }
          });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(
        new MessageResponse("user " + signupRequest.getUsername() + " is now registered"));
  }
}
