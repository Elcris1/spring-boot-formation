package com.example.demochatgpt.services;

import com.example.demochatgpt.exceptions.RoleAlreadyExistsException;
import com.example.demochatgpt.exceptions.RoleNotFoundException;
import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository repo){
        this.roleRepository = repo;
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findRoleByName(name).orElseThrow(RoleNotFoundException::new);
    }

    public Role createRole(Role role){
        if (this.roleRepository.findRoleByName(role.getName()).isPresent()) {
            throw new RoleAlreadyExistsException();
        }
        Role newRole = new Role();
        newRole.setName(role.getName());
        roleRepository.save(newRole);
        return newRole;
    }

    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }
}
