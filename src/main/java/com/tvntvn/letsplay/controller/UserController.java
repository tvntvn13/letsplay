package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.service.JwtService;
import com.tvntvn.letsplay.service.UserService;
import com.tvntvn.letsplay.util.InputSanitizer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

  @Autowired UserRepository repository;

  @Autowired UserService service;

  @Autowired JwtService jwtService;

  // @PostMapping
  // public ResponseEntity<User> createUser(@RequestBody User user) {
  //   return service.addUser(user);
  // }

  @GetMapping
  // @PreAuthorize("hasRole('user')")
  public ResponseEntity<List<User>> getUsers() {
    return service.findAllUsers();
  }

  @GetMapping(params = "id")
  // @PreAuthorize("hasRole('user')")
  public ResponseEntity<Object> getUser(@RequestParam String userId) {
    String clean = InputSanitizer.sanitize(userId);
    if (service.findUserById(clean).isPresent()) {
      return new ResponseEntity<>(service.findUserById(clean).get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>("user not found with id: " + userId, HttpStatus.NO_CONTENT);
    }
    // ereturn service.findUserById(clean).isPresent() ? service.findUserById(clean).get() : null;
  }

  // @GetMapping("/name/{name}")
  // public Optional<User> getUserByName(@PathVariable String name) {
  //   return service.findUserByName(name);
  // }

  // @GetMapping("/email/{email}")
  // public Optional<User> getUserByEmail(@PathVariable String email) {
  //   return service.findUserByEmail(email);
  // }

  @PutMapping("/update")
  // @PreAuthorize("hasRole('user')")
  public ResponseEntity<User> modifyUser(@RequestBody User user) {
    // TODO check the fields that are present and only update those.
    // sanitize inputs
    // return service.updateUser(user);
    return new ResponseEntity<User>(service.updateUser(user), HttpStatus.OK);
  }

  // @PreAuthorize("hasRole('admin')")
  @DeleteMapping(params = "id")
  public ResponseEntity<String> deleteUser(@RequestParam String id) {
    String clean = InputSanitizer.sanitize(id);
    return new ResponseEntity<String>("user deleted with id: " + clean, HttpStatus.OK);
  }
}
