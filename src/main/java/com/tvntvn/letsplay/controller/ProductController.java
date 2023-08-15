package com.tvntvn.letsplay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.service.ProductService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/products")
public class ProductController {
  @Autowired private ProductService service;

  @PostMapping
  @PreAuthorize("hasAutority('user)")
  public ResponseEntity<Object> createProduct(
      @RequestHeader("Authorization") String auth, @RequestBody Product user) {
    String token = auth.substring(7);
    return service.addProduct(user, token);
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return service.findAllProducts();
  }

  @GetMapping(params = "productId")
  @PreAuthorize("hasAutority('user)")
  public ResponseEntity<Object> getProduct(@RequestParam String productId) {
    return service.findProductById(productId);
  }

  @GetMapping(params = "name")
  @PreAuthorize("hasAutority('user)")
  public List<Product> getProductByName(@RequestParam String name) {
    return service.findProductByName(name);
  }

  @PutMapping("/update")
  @PreAuthorize("hasAutority('user')")
  public ResponseEntity<Object> modifyProduct(@RequestBody Product product) {
    return service.updateProduct(product);
  }

  @DeleteMapping("/{productId}")
  @PreAuthorize("hasAutority('admin')")
  public ResponseEntity<Object> deleteProduct(@PathVariable String productId) {
    return service.deleteProduct(productId);
  }
}
