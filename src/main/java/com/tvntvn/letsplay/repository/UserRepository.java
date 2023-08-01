package com.tvntvn.letsplay.repository;

import com.tvntvn.letsplay.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {}
