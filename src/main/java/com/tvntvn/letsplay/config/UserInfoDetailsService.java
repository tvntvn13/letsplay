// package com.tvntvn.letsplay.config;

// import com.tvntvn.letsplay.model.User;
// import com.tvntvn.letsplay.repository.UserRepository;
// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Component;

// @Component
// public class UserInfoDetailsService implements UserDetailsService {

//   @Autowired private UserRepository repository;

//   @Override
//   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//     Optional<User> user = repository.findByName(username);
//     return user.map(UserInfoDetails::new)
//         .orElseThrow(() -> new UsernameNotFoundException("user not found"));
//   }
// }
