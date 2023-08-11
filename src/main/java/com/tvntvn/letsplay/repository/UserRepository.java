package com.tvntvn.letsplay.repository;

import com.tvntvn.letsplay.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByName(String name);

  Optional<User> findByEmail(String email);
}
