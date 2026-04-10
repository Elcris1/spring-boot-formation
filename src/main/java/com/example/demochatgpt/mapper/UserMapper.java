package com.example.demochatgpt.mapper;
import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDto(User usuario);
    List<UserResponseDTO> toDtoList(List<User> users);

    User toEntity(UserCreateRequestDTO dto);
}
