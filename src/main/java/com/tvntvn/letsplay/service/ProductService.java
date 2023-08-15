package com.tvntvn.letsplay.service;

import java.util.List;
import java.util.Random;
// import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import jakarta.annotation.PostConstruct;
import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.repository.ProductRepository;

@Service
public class ProductService {

  @Autowired private ProductRepository repository;

  // @PostConstruct
  public void createProducts() {
    if (repository.findAll().isEmpty()) {
      List<Product> products =
          IntStream.rangeClosed(1, 50)
              .mapToObj(
                  i ->
                      Product.builder()
                          .name("product " + i)
                          .price(new Random().nextDouble(5000))
                          .description(i + " description")
                          .userId(String.valueOf(i))
                          .build())
              .collect(Collectors.toList());

      for (Product prod : products) {
        addProduct(prod);
      }
    }
  }

  public Product addProduct(Product product) {
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
