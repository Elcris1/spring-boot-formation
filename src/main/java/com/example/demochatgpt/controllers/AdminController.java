package com.example.demochatgpt.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @GetMapping("")
    public ResponseEntity<String> getDefault(){
        return ResponseEntity.ok("HELLO FROM ADMIN PANEL");
    }
}
