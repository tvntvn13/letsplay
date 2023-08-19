package com.tvntvn.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
  @JsonIgnore @Id private String id;

  @Field
  @NotBlank(message = "name cannot be empty")
  private String name;

  @Field
  @NotBlank(message = "description cannot be empty")
  private String description;

  @Field
  @NotNull(message = "price cannot be empty")
  private Double price;

  @Field
  @JsonIgnore
  @NotBlank(message = "userId cannot empty")
  private String userId;
}
