package com.tvntvn.letsplay.service;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired private UserRepository repository;

  // TODO CRUD OPERATIONS:

  public User addUser(User user) {
    user.setId(UUID.randomUUID().toString().split("-")[0]);
    return repository.save(user);
  }

  public List<User> findAllUsers() {
    return repository.findAll();
  }

  public User findUserById(String id) {
    return repository.findById(id).get();
  }

  public List<User> findUserByName(String name) {
    return repository.findByName(name);
  }

  public User findUserByEmail(String email) {
    return repository.findByEmail(email).get();
  }

  public User updateUser(User user) {
    User existingUser = repository.findById(user.getId()).get();
    existingUser.setName(user.getName());
    existingUser.setPassword(user.getPassword());
    existingUser.setEmail(user.getEmail());
    existingUser.setRole(user.getRole());
    return repository.save(existingUser);
  }

  public String deleteUser(String id) {
    repository.deleteById(id);
    return "user deleted with id " + id;
  }
}
