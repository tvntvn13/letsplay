package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.model.Product;
import com.tvntvn.letsplay.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/products")
public class ProductController {
  @Autowired private ProductService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product createProduct(@RequestBody Product user) {
    return service.addProduct(user);
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return service.findAllProducts();
  }

  @GetMapping(params = "productId")
  public Product getProduct(@RequestParam String productId) {
    return service.findProductById(productId);
  }

  @GetMapping(params = "name")
  public List<Product> getProductByName(@RequestParam String name) {
    return service.findProductByName(name);
  }

  @PutMapping("/update")
  public Product modifyProduct(@RequestBody Product product) {
    return service.updateProduct(product);
  }

  @DeleteMapping("/{productId}")
  public String deleteProduct(@PathVariable String productId) {
    return service.deleteProduct(productId);
  }
}
