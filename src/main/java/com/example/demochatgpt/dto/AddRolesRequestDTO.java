package com.example.demochatgpt.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddRolesRequestDTO {
    private List<String> roles;
}
