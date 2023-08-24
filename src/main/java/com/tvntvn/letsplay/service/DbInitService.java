package com.tvntvn.letsplay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tvntvn.letsplay.config.DummyUserProperties;
import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.ProductRepository;
import com.tvntvn.letsplay.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DbInitService {

  // TODO: fix the autowiring so dummy not null!
  private DummyUserProperties dummyUser;

  @Autowired private UserRepository userRepository;

  @Autowired private ProductRepository productRepository;

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  // flag for init or not
  private boolean doit = true;

  @Autowired
  public void setDummyUser(DummyUserProperties userProperties) {
    this.dummyUser = userProperties;
  }

  @PostConstruct
  public void initializeUsersToDb() {

    User bobby =
        new User(
            dummyUser.getUser1Name(),
            dummyUser.getUser1Email(),
            encoder.encode(dummyUser.getUser1Password()));
    User damon =
        new User(
            dummyUser.getUser2Name(),
            dummyUser.getUser2Email(),
            encoder.encode(dummyUser.getUser2Password()));
    User tom =
        new User(
            dummyUser.getUser3Name(),
            dummyUser.getUser3Email(),
            encoder.encode(dummyUser.getUser3Password()));

    User[] users = {bobby, damon, tom};

    for (User user : users) {
      if (userIsNotPresent(user) && doit) {
        user.setRole("user");
        userRepository.save(user);

        for (int i = 1; i < 4; i++) {
          Product prod = new Product();
          prod.setName(user.getName() + "s product" + i);
          prod.setDescription(prod.getName() + "s description");
          prod.setPrice(12.3 * i * 1.5);

          User dbUser = userRepository.findByName(user.getName()).get();
          prod.setUserId(dbUser.getId());
          productRepository.save(prod);
        }
      }
    }
    System.out.println("database initialized with some users and products");
  }

  private boolean userIsNotPresent(User user) {
    User check = userRepository.findByName(user.getName()).orElse(null);
    return check == null;
  }
}
