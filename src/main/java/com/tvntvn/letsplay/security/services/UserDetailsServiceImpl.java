package com.tvntvn.letsplay.security.services;

import com.tvntvn.letsplay.model.User;
import com.tvntvn.letsplay.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<User> users = userRepository.findByName(username);
    if (users == null || users.size() == 0) {
      throw new UsernameNotFoundException("user not found with name: " + username);
    }
    return UserDetailsImpl.build(users.get(0));
  }
}
