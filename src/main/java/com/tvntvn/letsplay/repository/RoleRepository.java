package com.tvntvn.letsplay.repository;

import com.tvntvn.letsplay.model.Role;
import com.tvntvn.letsplay.model.RoleEnum;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(RoleEnum name);
}
