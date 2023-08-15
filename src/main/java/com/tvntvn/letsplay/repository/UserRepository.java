package com.tvntvn.letsplay.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tvntvn.letsplay.model.User;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByName(String name);

  Optional<User> findByEmail(String email);
}
