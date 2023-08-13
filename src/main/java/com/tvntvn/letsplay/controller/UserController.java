package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.service.UserService;
import com.tvntvn.letsplay.util.InputSanitizer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {
  @Autowired private UserService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User createUser(@RequestBody User user) {
    return service.addUser(user);
  }

  @GetMapping
  public List<User> getUsers() {
    return service.findAllUsers();
  }

  @GetMapping(params = "id")
  public User getUser(@RequestParam String userId) {
    String clean = InputSanitizer.sanitize(userId);
    return service.findUserById(clean).isPresent() ? service.findUserById(clean).get() : null;
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
  public User modifyUser(@RequestBody User user) {
    // TODO check the fields that are present and only update those.
    // sanitize inputs
    return service.updateUser(user);
  }

  @DeleteMapping(params = "id")
  public String deleteUser(@RequestParam String id) {
    String clean = InputSanitizer.sanitize(id);
    return service.deleteUser(clean);
  }
}
