package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {
  @Size(min = 3, max = 60)
  @NotBlank(message = "name cannot be empty")
  private String name;

  @Size(min = 3, max = 70)
  @NotBlank(message = "description cannot be empty")
  private String description;

  @Positive(message = "price cannot be smaller than 0")
  @NotNull(message = "price cannot be empty")
  private Double price;
}
