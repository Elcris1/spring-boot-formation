package com.example.demochatgpt.controllers;

import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.services.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/role")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createRole(
            @RequestBody Role role ) {
        var newRole = this.roleService.createRole(role);

        URI location = URI.create("/roles/" + newRole.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("")
    public ResponseEntity<List<Role>> getRoles() {
        var roles = this.roleService.getRoles();
        return ResponseEntity.ok(roles);
    }
}
