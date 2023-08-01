package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ProductController {
  @Autowired private ProductService service;
}
