package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
  @NotBlank(message = "name cannot be empty")
  private String name;

  @NotBlank(message = "description cannot be empty")
  private String description;

  @NotNull(message = "price cannot be empty")
  private Double price;
}
