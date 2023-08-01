package com.tvntvn.letsplay.service;

import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  @Autowired private ProductRepository repository;

  // TODO CRUD OPERATIONS:

  public Product addProduct(Product product) {
    product.setId(UUID.randomUUID().toString().split("-")[0]);
    return repository.save(product);
  }

  public List<Product> findAllProducts() {
    return repository.findAll();
  }

  public Product findProductById(String id) {
    return repository.findById(id).get();
  }

  public List<Product> findProductByName(String name) {
    return repository.findByName(name);
  }

  public Product updateProduct(Product product) {
    Product existingProduct = repository.findById(product.getId()).get();
    existingProduct.setName(product.getName());
    existingProduct.setPrice(product.getPrice());
    existingProduct.setDescription(product.getDescription());
    return repository.save(existingProduct);
  }

  public String deleteProduct(String id) {
    repository.deleteById(id);
    return "product deleted with id " + id;
  }
}
