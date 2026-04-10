package com.example.demochatgpt.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String name;
    private String email;
    private Long createdAt;
}
