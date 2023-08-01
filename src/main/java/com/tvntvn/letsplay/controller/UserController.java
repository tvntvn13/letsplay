package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
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

  @GetMapping("/{userId}")
  public User getUser(@PathVariable String userId) {
    return service.findUserById(userId);
  }

  @GetMapping("name/{name}")
  public List<User> getUserByName(@PathVariable String name) {
    return service.findUserByName(name);
  }

  @GetMapping("email/{email}")
  public User getUserByEmail(@PathVariable String email) {
    return service.findUserByEmail(email);
  }

  @PutMapping
  public User modifyUser(@RequestBody User user) {
    return service.updateUser(user);
  }

  @DeleteMapping("/{userId}")
  public String deleteUser(@PathVariable String userId) {
    return service.deleteUser(userId);
  }
}
