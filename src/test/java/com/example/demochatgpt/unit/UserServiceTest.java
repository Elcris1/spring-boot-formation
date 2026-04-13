package com.example.demochatgpt.unit;

import com.example.demochatgpt.dto.DetailedUserResponseDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.exceptions.UserNotFoundException;
import com.example.demochatgpt.mapper.UserMapper;
import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.UserRepository;
import com.example.demochatgpt.services.RoleService;
import com.example.demochatgpt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    RoleService roleService;

    @InjectMocks
    UserService userService;

    @Test
    void shouldReturnUserById() {
        // Arrange
        Long id = 1L;

        User user = new User();
        user.setId(id);
        user.setEmail("test@test.com");

        UserResponseDTO dto = new UserResponseDTO();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        // Act
        UserResponseDTO result = userService.getUserById(id);

        // Assert
        assertNotNull(result);
        assertEquals(dto, result);

        verify(userRepository).findById(id);
        verify(userMapper).toDto(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(id));

        verify(userRepository).findById(id);
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldReturnDetailedUserById() {
        // Arrange
        Long id = 1L;

        User user = new User();
        user.setId(id);
        user.setEmail("test@test.com");
        user.setName("test");
        user.setRoles(Set.of());

        DetailedUserResponseDTO dto = new DetailedUserResponseDTO();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDetailedDto(user)).thenReturn(dto);

        // Act
        DetailedUserResponseDTO result = userService.getDetailedUserById(id);

        // Assert
        assertNotNull(result);
        assertEquals(dto, result);

        verify(userRepository).findById(id);
        verify(userMapper).toDetailedDto(user);
    }

    @Test
    void shouldThrowExceptionWhenDetailedUserNotFound() {
        // Arrange
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.getDetailedUserById(id));

        verify(userRepository).findById(id);
        verifyNoInteractions(userMapper);
    }

}
