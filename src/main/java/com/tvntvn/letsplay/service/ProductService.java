package com.tvntvn.letsplay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

// import jakarta.annotation.PostConstruct;
import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.model.ProductRequest;
import com.tvntvn.letsplay.model.User;
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

  public ResponseEntity<Object> addProduct(ProductRequest product, String token) {
    
    User user = userRepository.findByName(jwtService.extractUsername(token)).orElse(null);
    String userId = user != null ? user.getId() : null;
    String name = input.sanitize(product.getName());
    String description = input.sanitize(product.getDescription());
    Double price = product.getPrice();
    
    if(userId == null || userId.equals("")) return formatter.format("bad credentials", HttpStatus.BAD_REQUEST);
    if(name.equals("")) return formatter.format("name is required", HttpStatus.BAD_REQUEST);
    if(!repository.findByName(name).isEmpty()) return formatter.format("name is already taken",name, HttpStatus.CONFLICT);
    if(description.equals("")) return formatter.format("description is required", HttpStatus.BAD_REQUEST);
    if(price == null || price == 0.0) return formatter.format("price is required", HttpStatus.BAD_REQUEST);
    
    Product newProduct = new Product();
    
    newProduct.setName(name);
    newProduct.setPrice(price);
    newProduct.setDescription(description);
    newProduct.setUserId(userId);
    repository.save(newProduct);

    return formatter.format(repository.save(newProduct),HttpStatus.OK);
  }

    public ResponseEntity<Object> findAllProducts() {
    List<Product> products = repository.findAll();
    if(products.isEmpty()) return formatter.format("no products were found", HttpStatus.NO_CONTENT);
    return formatter.format(products, HttpStatus.OK);
  }

  public ResponseEntity<Object> findProductById(String id) {
    String clean = input.sanitize(id);
    Product product = repository.findByName(clean).orElse(null);
    if(clean.equals("") || product == null) return formatter.format("no product found by id "+clean, HttpStatus.NOT_FOUND);
    return formatter.format(product, HttpStatus.OK);
  }

  public ResponseEntity<Object> findProductByName(String name) {
    String clean = input.sanitize(name);
    if(!repository.findByName(clean).isPresent()) return formatter.format("product "+clean+" not found", HttpStatus.NOT_FOUND);
    Product product = repository.findByName(clean).orElse(null);
    if(product != null){
      return formatter.format(product, HttpStatus.OK);
    }else{
      return formatter.format("product "+clean+" not found", HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Object> updateProduct(String token, String name, Product product) {
    String clean = input.sanitize(name);
    Product existingProduct = repository.findByName(clean).orElse(null);
    if(clean.equals("") || existingProduct == null) return formatter.format("product not found with name "+clean, HttpStatus.NOT_FOUND);

    String userName = jwtService.extractUsername(token);
    User owner = userRepository.findByName(userName).orElse(null);
    if(userName.equals("") || owner == null){
      return formatter.format("bad credentials", HttpStatus.FORBIDDEN);
    }

    String oldName = existingProduct.getName();
    String oldDescription = existingProduct.getDescription();
    Double oldPrice = existingProduct.getPrice();

    Product newProduct = new Product();
    String userId = owner.getId();
    if(product.getName() == null) newProduct.setName(oldName);
    if(product.getPrice() == null) newProduct.setPrice(oldPrice);
    if(product.getDescription() == null) newProduct.setDescription(oldDescription);
    newProduct.setUserId(userId);
    
    repository.save(newProduct);
    return formatter.format("product "+oldName+" updated", HttpStatus.OK);
  }

  public ResponseEntity<Object> deleteProduct(String name, String token) {
    User user = userRepository.findByName(jwtService.extractUsername(token)).orElse(null);
    if(user == null) return formatter.format("bad credentials", HttpStatus.FORBIDDEN);
    String userId = user.getId();

    String clean = input.sanitize(name);
    Product toDelete; 
    if(repository.findByName(clean).isPresent()){
      toDelete = repository.findByName(clean).get();
    }else{
      return formatter.format(clean+" not found", HttpStatus.BAD_REQUEST);
    }
    
    List<SimpleGrantedAuthority> authorities = user.getRoles();
    Boolean isValid = userId.equals(toDelete.getUserId()) || authorities.stream().anyMatch(auth -> auth.getAuthority().equals("admin"));
    if(!isValid) return formatter.format("no rights for deleting product "+clean, HttpStatus.FORBIDDEN);

    if(repository.findByName(clean).isEmpty()){
      return formatter.format(clean+" not found", HttpStatus.BAD_REQUEST);
    }
    repository.deleteByName(name);
    return formatter.format("product deleted with id " + name, HttpStatus.OK);
  }
}
