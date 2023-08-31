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

  private DummyUserProperties dummyUser;

  @Autowired private UserRepository userRepository;

  @Autowired private ProductRepository productRepository;

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  // flag to set init ON/OFF
  private boolean init = false;

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

    User taneli = new User("taneli", "taneli@taneli.com", encoder.encode("test123"));

    User[] users = {bobby, damon, tom, taneli};

    for (User user : users) {
      if (userIsNotPresent(user) && init) {
        user.setRole("user");
        userRepository.save(user);

        for (int i = 1; i <= 6; i++) {
          Product prod = new Product();
          prod.setName(user.getName() + "s product" + i);
          prod.setDescription(prod.getName() + "s description");
          prod.setPrice(Double.valueOf(Math.round(12.3 * i * 1.55) * 100 / 100));

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
