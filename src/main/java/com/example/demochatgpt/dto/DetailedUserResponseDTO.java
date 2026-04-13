package com.example.demochatgpt.dto;

import com.example.demochatgpt.models.Role;
import lombok.Data;

import java.util.Set;

@Data
public class DetailedUserResponseDTO{
    private Long id;
    private String name;
    private String email;
    private Long createdAt;
    private Set<Role> roles;
}
