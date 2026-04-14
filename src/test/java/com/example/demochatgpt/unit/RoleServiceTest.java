package com.example.demochatgpt.unit;

import com.example.demochatgpt.exceptions.RoleAlreadyExistsException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        when(roleRepository.findRoleByName(name)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class,
                () -> roleService.getRoleByName(name));

        verify(roleRepository).findRoleByName(name);
    }

    @Test
    void createRole_shouldCreateRole() {
        // Arrange
        String name = "ROLE";

        Role role = new Role();
        role.setName(name);

        when(roleRepository.findRoleByName(name)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        Role result = roleService.createRole(role);

        // Assert
        assertNotNull(result);
        assertEquals(role, result);

        verify(roleRepository).findRoleByName(name);
        verify(roleRepository).save(any(Role.class));
    }


    @Test
    void createRole_shouldThrowRoleAlreadyExists_whenRoleExists() {
        // Arrange
        String name = "ROLE";

        Role role = new Role();
        role.setName(name);

        when(roleRepository.findRoleByName(name)).thenReturn(Optional.of(role));

        // Act
        assertThrows(RoleAlreadyExistsException.class,
                () -> roleService.createRole(role));

        verify(roleRepository).findRoleByName(name);
        verify(roleRepository, never()).save(any());
    }

    @Test
    void getRoles_shouldReturnRolesList() {
        List<Role> roles = List.of(new Role(), new Role());

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> res = roleService.getRoles();

        assertNotNull(res);
        assertEquals(res, roles);

        verify(roleRepository).findAll();
    }

    @Test
    void getRoles_shouldReturnEmptyRolesList_whenNoRoles() {
        List<Role> roles = List.of();

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> res = roleService.getRoles();

        assertNotNull(res);
        assertEquals(res, roles);

        verify(roleRepository).findAll();
    }


}
