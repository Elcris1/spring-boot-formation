package com.example.demochatgpt.dto;

import lombok.Data;

@Data
public class DetailedUserResponseDTO{
    private Long id;
    private String name;
    private String email;
    private Long createdAt;
}
