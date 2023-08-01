package com.tvntvn.letsplay.repository;

import com.tvntvn.letsplay.model.Product;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

  List<Product> findByName(String name);
}
