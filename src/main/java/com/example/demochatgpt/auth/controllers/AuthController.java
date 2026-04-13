package com.example.demochatgpt.auth.controllers;

import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserLoginDTO;
import com.example.demochatgpt.services.UserService;
import com.example.demochatgpt.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(JwtUtils config, UserService userService) {
        this.jwtUtils = config;
        this.userService = userService;
    }

    @PostMapping ("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO rq) {
        var user = userService.validateUser(rq);
        var token = jwtUtils.generateToken(user.getEmail());
        return ResponseEntity.ok(token);
    }
}
