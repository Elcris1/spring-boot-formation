package com.example.demochatgpt.services;

import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository repo){
        this.roleRepository = repo;
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findRoleByName(name).orElseThrow(RuntimeException::new);
    }

    public Role createRole(String roleName){
        if (this.roleRepository.findRoleByName(roleName).isPresent()) {
            throw new RuntimeException("Role already exits");
        }

        Role role = new Role();
        role.setName(roleName);
        return role;
    }
}
