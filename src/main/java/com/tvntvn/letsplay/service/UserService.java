package com.tvntvn.letsplay.service;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
// import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// @Component

@Service
public class UserService implements UserDetailsService {

  private final UserRepository repository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.repository = userRepository;
  }

  // public UserDetailsService userDetailsService() {
  //   return new UserService();
  // }

  // private PasswordEncoder encoder = new BCryptPasswordEncoder();

  // FIXME! fix the method logic

  // public ResponseEntity<String> addUser(User user) {
  //   // user.setId(UUID.randomUUID().toString().split("-")[0]);
  //   String name = InputSanitizer.sanitize(user.getName());
  //   String email = InputSanitizer.sanitize(user.getEmail());
  //   String role = "user";
  //   String passwrod = encoder.encode(user.getPassword());

  //   if(findUserByName(name).isPresent()){
  //     return new ResponseEntity<String>("", headers, rawStatus)
  //   }
  //
  //   return repository.save(user);
  // }

  public ResponseEntity<List<User>> findAllUsers() {
    return new ResponseEntity<List<User>>(repository.findAll(), HttpStatus.OK);
  }

  // public String notFound(){
  //   return
  // }

  public Optional<User> findUserById(String id) {
    return repository.findById(id);
  }

  public Optional<User> findUserByName(String name) {
    return repository.findByName(name);
  }

  public Optional<User> findUserByEmail(String email) {
    return repository.findByEmail(email);
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

  @PostConstruct
  private void initAdmin() {
    if (repository.findByName("admin").isEmpty()) {
      User admin = new User("admin", "admin@admin.com", "root", "admin");
      repository.save(admin);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = repository.findByName(username);
    if (user.isPresent()) return user.get();
    throw new UsernameNotFoundException("user not found");
  }
}
