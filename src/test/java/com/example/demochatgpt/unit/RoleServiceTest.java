package com.example.demochatgpt.unit;

import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.exceptions.RoleNotFoundException;
import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.RoleRepository;
import com.example.demochatgpt.services.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    @Test
    void getRoleByName_shouldReturnCorrectRole() {
        // Arrange
        String name = "ROLE";

        Role role = new Role();
        role.setName(name);

        UserResponseDTO dto = new UserResponseDTO();

        when(roleRepository.findRoleByName(name)).thenReturn(Optional.of(role));

        // Act
        Role result = roleService.getRoleByName(name);

        // Assert
        assertNotNull(result);
        assertEquals(role, result);

        verify(roleRepository).findRoleByName(name);
    }

    @Test
    void getRoleByName_shouldThrowRoleNotFound_whenRoleDoesntExist() {
        // Arrange
        String name = "ROLE";

        UserResponseDTO dto = new UserResponseDTO();

        when(roleRepository.findRoleByName(name)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class,
                () -> roleService.getRoleByName(name));

        verify(roleRepository).findRoleByName(name);
    }
}
