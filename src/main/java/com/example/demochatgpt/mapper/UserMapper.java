package com.example.demochatgpt.mapper;
import com.example.demochatgpt.dto.DetailedUserResponseDTO;
import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserLoginDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDto(User user);
    List<UserResponseDTO> toDtoList(List<User> users);
    DetailedUserResponseDTO toDetailedDto(User user);
    List<DetailedUserResponseDTO> toDetailedDtoList(List<User> users);

    User toEntity(UserCreateRequestDTO dto);
    User toEntity(UserLoginDTO dto);
}
