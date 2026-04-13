package com.example.demochatgpt.controllers;

import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.services.RoleService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("")
    public ResponseEntity<Void> createRole(
            @RequestBody String name ) {
        var role = this.roleService.createRole(name);

        URI location = URI.create("/roles/" + role.getId());
        return ResponseEntity.created(location).build();

    }
}
