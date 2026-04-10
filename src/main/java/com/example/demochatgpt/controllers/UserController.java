package com.example.demochatgpt.controllers;

import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demochatgpt.models.User;
import com.example.demochatgpt.services.UserService;

import java.net.URI;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/")
    public String getMethodName() {
        return "Hello world!";
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    
    @PostMapping("/user")
    public ResponseEntity<Void> postMethodName(@RequestBody UserCreateRequestDTO user) {
        var res = userService.createUser(user);

         URI location = URI.create("/usuarios/" + res.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
    
}
