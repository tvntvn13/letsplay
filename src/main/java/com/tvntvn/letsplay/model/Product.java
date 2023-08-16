package com.tvntvn.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
// import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Builder
@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  @Id private String id;

  @Field
  @NotBlank(message = "name cannot be empty")
  private String name;

  @Field
  @NotBlank(message = "description cannot be empty")
  private String description;

  @Field
  @NotBlank(message = "price cannot be empty")
  private Double price;

  @Field
  @NotBlank(message = "userId cannot empty")
  private String userId;
}
