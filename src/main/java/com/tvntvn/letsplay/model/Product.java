package com.tvntvn.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@Data
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
  @Positive
  private Double price;

  @Field
  @JsonIgnore
  @NotBlank(message = "userId cannot empty")
  private String userId;
}
