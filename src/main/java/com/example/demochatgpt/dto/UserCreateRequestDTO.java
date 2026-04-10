package com.example.demochatgpt.dto;

import lombok.Data;

@Data
public class UserCreateRequestDTO {
    private String email;
    private String password;
}
