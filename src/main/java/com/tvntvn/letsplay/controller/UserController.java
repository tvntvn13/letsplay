package com.tvntvn.letsplay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.service.JwtService;
import com.tvntvn.letsplay.service.UserService;
import com.tvntvn.letsplay.util.InputSanitizer;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

  @Autowired UserRepository repository;

  @Autowired UserService service;

  @Autowired JwtService jwtService;

  @Autowired InputSanitizer s;

  @GetMapping
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<List<User>> getUsers() {
    return service.findAllUsers();
  }

  @GetMapping(params = "id")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getUserById(@RequestParam String id) {
    return service.findUserById(id);
  }

  @GetMapping(params = "name")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getUserByName(@RequestParam String name) {
    return service.findUserByName(name);
  }

  @GetMapping(params = "email")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getUserByEmail(@RequestParam String email) {
    return service.findUserByEmail(email);
  }

  @PutMapping("/update")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> modifyUser(
      @RequestHeader("Authorization") String header, @RequestBody User user) {

    String token = header.substring(7);
    return service.updateUser(user, token);
  }

  @PreAuthorize("hasAuthority('admin')")
  @DeleteMapping(params = "id")
  public ResponseEntity<Object> deleteUser(@RequestParam String id) {
    String clean = s.sanitize(id);
    return service.deleteUser(clean);
  }
}
