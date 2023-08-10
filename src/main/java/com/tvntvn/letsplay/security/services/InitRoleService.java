package com.tvntvn.letsplay.security.services;

import com.tvntvn.letsplay.model.Role;
import com.tvntvn.letsplay.model.RoleEnum;
import com.tvntvn.letsplay.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitRoleService {

  @Autowired private RoleRepository roleRepository;

  @PostConstruct
  public void initRoles() {
    for (RoleEnum roleName : RoleEnum.values()) {
      if (roleRepository.findByName(roleName).isEmpty()) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
        System.out.println("role " + roleName + " created");
      }
    }
  }
}
