package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {
  @NotBlank(message = "name cannot be empty")
  private String name;

  @NotBlank(message = "description cannot be empty")
  private String description;

  @NotBlank(message = "price cannot be empty")
  private Double price;
}
