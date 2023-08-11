// package com.tvntvn.letsplay.security.services;

// import com.tvntvn.letsplay.model.User;
// import com.tvntvn.letsplay.repository.UserRepository;
// import jakarta.annotation.PostConstruct;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class InitUsersService {

//   @Autowired private UserRepository userRepository;

//   @PostConstruct
//   public void initUsers() {
//     if (userRepository.findAll().isEmpty()) {
//       User user = new User();
//       userRepository.save(user);
//       System.out.println("user collection created");
//     }
//   }
// }
