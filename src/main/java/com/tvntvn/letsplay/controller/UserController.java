package com.tvntvn.letsplay.controller;

import com.tvntvn.letsplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {
  @Autowired private UserService service;
}
