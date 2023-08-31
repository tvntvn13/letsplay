package com.tvntvn.letsplay.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateRequest {
  @Size(min = 3, max = 60)
  private String name;

  @Size(min = 3, max = 70)
  String description;

  @Positive private Double price;
}
