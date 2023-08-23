package com.tvntvn.letsplay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

  @Autowired UserRepository repository;

  @Autowired UserService service;

  @Autowired JwtService jwtService;

  @Autowired InputSanitizer s;

  @GetMapping
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getUsers() {
    return service.findAllUsers();
  }

  @GetMapping(params = "id")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getUserById(@RequestParam String id) {
    return service.findUserById(id);
  }

  @GetMapping(path = "/whoami")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getCurrentUser(@RequestHeader("Authorization") String header) {
    String token = header.substring(7);
    String username = jwtService.extractUsername(token);
    return service.findUserByName(username);
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

  @PutMapping
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> modifyUser(
      @RequestHeader("Authorization") String header, @RequestBody User user) {

    String token = header.substring(7);
    return service.updateUser(user, token);
  }

  @PreAuthorize("hasAuthority('admin')")
  @DeleteMapping(params = "name")
  public ResponseEntity<Object> deleteUser(
      @RequestHeader("Authorization") String header, @RequestParam String name) {
    String token = header.substring(7);
    String clean = s.sanitize(name);
    return service.deleteUser(clean, token);
  }

  @PreAuthorize("hasAuthority('admin')")
  @PostMapping(path = "/admin", params = "name")
  public ResponseEntity<Object> addAdmin(@RequestParam String name) {
    return service.addAdminRights(name);
  }

  @PreAuthorize("hasAuthority('admin')")
  @DeleteMapping(path = "/admin", params = "name")
  public ResponseEntity<Object> removeAdmin(
      @RequestHeader("Authorization") String header, @RequestParam String name) {
    String token = header.substring(7);
    return service.removeAdminRights(name, token);
  }
}
