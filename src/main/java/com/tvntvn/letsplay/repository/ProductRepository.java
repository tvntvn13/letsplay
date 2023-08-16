package com.tvntvn.letsplay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tvntvn.letsplay.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

  Optional<Product> findByName(String name);

  List<Product> findAllByUserId(String userId);

  void deleteByName(String name);
}
