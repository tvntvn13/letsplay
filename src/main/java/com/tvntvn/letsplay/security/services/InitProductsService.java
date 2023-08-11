// package com.tvntvn.letsplay.security.services;

// import com.tvntvn.letsplay.model.Product;
// import com.tvntvn.letsplay.repository.ProductRepository;
// import jakarta.annotation.PostConstruct;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class InitProductsService {

//   @Autowired private ProductRepository productRepository;

//   @PostConstruct
//   public void initProducts() {
//     if (productRepository.findAll().isEmpty()) {
//       Product product = new Product();
//       productRepository.save(product);
//       System.out.println("product collection created");
//     }
//   }
// }
