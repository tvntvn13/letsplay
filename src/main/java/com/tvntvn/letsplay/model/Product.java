package com.tvntvn.letsplay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
@Data
@AllArgsConstructor
public class Product {
  @Id private String id;
  private String name;
  private String description;
  private Double price;
  @DBRef private String userId;
}
