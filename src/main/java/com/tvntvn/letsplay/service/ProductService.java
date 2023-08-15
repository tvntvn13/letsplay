package com.tvntvn.letsplay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// import jakarta.annotation.PostConstruct;
import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.repository.ProductRepository;
import com.tvntvn.letsplay.repository.UserRepository;
import com.tvntvn.letsplay.util.InputSanitizer;
import com.tvntvn.letsplay.util.ResponseFormatter;

@Service
public class ProductService {

@Autowired private UserRepository userRepository;

  @Autowired private ProductRepository repository;

  @Autowired private JwtService jwtService;

  @Autowired private InputSanitizer input;

  @Autowired private ResponseFormatter formatter;

  //TODO create dummy products.
  // @PostConstruct
  // public void createProducts() {
  //   if (repository.findAll().isEmpty()) {
  //     List<Product> products =
  //         IntStream.rangeClosed(1, 50)
  //             .mapToObj(
  //                 i ->
  //                     Product.builder()
  //                         .name("product " + i)
  //                         .price(new Random().nextDouble(5000))
  //                         .description(i + " description")
  //                         .userId(String.valueOf(i))
  //                         .build())
  //             .collect(Collectors.toList());

  //     for (Product prod : products) {
  //       addProduct(prod, token);
  //     }
  //   }
  // }

  public ResponseEntity<Object> addProduct(Product product, String token) {
    
    String userId = userRepository.findByName(jwtService.extractUsername(token)).get().getId();
    String name = product.getName();
    String description = product.getDescription();
    Double price = product.getPrice();
    
    if(userId == null) return formatter.format("bad credentials", HttpStatus.BAD_REQUEST);
    if(input.sanitize(name).equals("")) return formatter.format("name is required", HttpStatus.BAD_REQUEST);
    if(input.sanitize(description).equals("")) return formatter.format("description is required", HttpStatus.BAD_REQUEST);
    if(price == null || price == 0.0) return formatter.format("price is required", HttpStatus.BAD_REQUEST);
    
    Product newProduct = new Product();
    
    newProduct.setName(name);
    newProduct.setPrice(price);
    newProduct.setDescription(description);
    newProduct.setUserId(userId);
    repository.save(newProduct);

    return formatter.format(repository.save(product),HttpStatus.OK);
  }

  public List<Product> findAllProducts() {
    return repository.findAll();
  }

  public ResponseEntity<Object> findProductById(String id) {
    return formatter.format(repository.findById(id).get(), HttpStatus.OK);
  }

  public List<Product> findProductByName(String name) {
    return repository.findByName(name);
  }

  public ResponseEntity<Object> updateProduct(Product product) {
    Product existingProduct = repository.findById(product.getId()).get();
    existingProduct.setName(product.getName());
    existingProduct.setPrice(product.getPrice());
    existingProduct.setDescription(product.getDescription());
    return formatter.format(repository.save(existingProduct), HttpStatus.OK);
  }

  public ResponseEntity<Object> deleteProduct(String id) {
    repository.deleteById(id);
    return formatter.format("product deleted with id " + id, HttpStatus.OK);
  }
}
