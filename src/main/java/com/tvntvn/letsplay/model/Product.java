package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "products")
@Data
public class Product {
  @Id private String id;
  @NotBlank private String name;
  @NotBlank private String description;
  @NotBlank private Double price;
  @NotBlank private String userId;
}
