package com.tvntvn.letsplay.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tvntvn.letsplay.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

  List<Product> findByName(String name);

  List<Product> findAllByUserId(String userId);
}
