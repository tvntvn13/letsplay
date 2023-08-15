package com.tvntvn.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
// import lombok.Builder;
import lombok.Data;

// @Builder
@Document(collection = "products")
@Data
public class Product {
  @Id private String id;
  @NotBlank @Field private String name;
  @NotBlank @Field private String description;
  @NotBlank @Field private Double price;
  @NotBlank @Field private String userId;

  public Product() {}
}
