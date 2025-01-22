package com.tvntvn.letsplay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tvntvn.letsplay.model.ProductRequest;
import com.tvntvn.letsplay.model.ProductUpdateRequest;
import com.tvntvn.letsplay.service.JwtService;
import com.tvntvn.letsplay.service.ProductService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/products")
public class ProductController {

  private final ProductService service;

  private final JwtService jwtService;

  @Autowired
  public ProductController(ProductService service, JwtService jwtService) {
    this.service = service;
    this.jwtService = jwtService;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> createProduct(
      @RequestHeader("Authorization") String auth, @Valid @RequestBody ProductRequest product) {
    String token = auth.substring(7);
    return service.addProduct(product, token);
  }

  @GetMapping
  public ResponseEntity<Object> getAllProducts() {
    return service.findAllProducts();
  }

  @GetMapping("/myproducts")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getCurrentUsersProducts(
      @RequestHeader("Authorization") String header) {
    String token = header.substring(7);
    String username = jwtService.extractUsername(token);
    return service.findAllByOwner(username);
  }

  @GetMapping(params = "owner")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getProductByUserName(@RequestParam String owner) {
    return service.findAllByOwner(owner);
  }

  @GetMapping(params = "name")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> getProductByName(@RequestParam String name) {
    return service.findProductByName(name);
  }

  @PutMapping(params = "name")
  @PreAuthorize("hasAuthority('user')")
  public ResponseEntity<Object> modifyProduct(
      @RequestHeader("Authorization") String header,
      @RequestParam String name,
      @Valid @RequestBody ProductUpdateRequest product) {
    String token = header.substring(7);
    return service.updateProduct(token, name, product);
  }

  @DeleteMapping(params = "name")
  @PreAuthorize("hasAnyAuthority('admin','user')")
  public ResponseEntity<Object> deleteProduct(
      @RequestHeader("Authorization") String header, @RequestParam String name) {
    String token = header.substring(7);
    return service.deleteProduct(name, token);
  }
}
