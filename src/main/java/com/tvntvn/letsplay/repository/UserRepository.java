package com.tvntvn.letsplay.repository;

import com.tvntvn.letsplay.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

  List<User> findByName(String name);

  Optional<User> findByEmail(String email);
}
