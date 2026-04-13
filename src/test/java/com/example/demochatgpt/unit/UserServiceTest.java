package com.example.demochatgpt.unit;

import com.example.demochatgpt.dto.DetailedUserResponseDTO;
import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.exceptions.InvalidFieldsException;
import com.example.demochatgpt.exceptions.UserAlreadyExistsException;
import com.example.demochatgpt.exceptions.UserNotFoundException;
import com.example.demochatgpt.mapper.UserMapper;
import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.UserRepository;
import com.example.demochatgpt.services.RoleService;
import com.example.demochatgpt.services.UserService;
import jakarta.validation.ConstraintViolationException;
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


    @Test
    void shouldReturnUserByEmail() {
        // Arrange
        String email = "test@test.com";
        long id = 1L;

        User user = new User();
        user.setId(id);
        user.setEmail(email);


        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserByEmailNotFound() {
        // Arrange
        String email = "test@test.com";

        Long id = 1L;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserByEmail(email));

        verify(userRepository).findByEmail(email);
    }

    @Test
    void createUserShouldCreateUserSuccessfully() {

        // Arrange
        UserCreateRequestDTO rq = new UserCreateRequestDTO();
        rq.setEmail("test@test.com");
        rq.setPassword("1234");

        Role role = new Role();
        role.setName("USER");

        User savedUser = new User();
        savedUser.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        when(roleService.getRoleByName("USER"))
                .thenReturn(role);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        User result = userService.createUser(rq);

        // Assert
        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        assertEquals(savedUser, result);


        verify(userRepository).findByEmail("test@test.com");
        verify(roleService).getRoleByName("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        UserCreateRequestDTO rq = new UserCreateRequestDTO();
        rq.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(rq));

        verify(userRepository).findByEmail("test@test.com");
        verifyNoInteractions(roleService);
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUserShouldThrowInvalidFieldsExceptionWhenConstraintViolationOccurs() {

        // Arrange
        UserCreateRequestDTO rq = new UserCreateRequestDTO();
        rq.setEmail("test@test.com");
        rq.setPassword("1234");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        when(roleService.getRoleByName("USER"))
                .thenThrow(new ConstraintViolationException(null));

        // Act & Assert
        assertThrows(InvalidFieldsException.class,
                () -> userService.createUser(rq));

        verify(userRepository).findByEmail("test@test.com");
        verify(roleService).getRoleByName("USER");
    }

    @Test
    void deleteUserShouldDeleteUserSuccessfully() {

        // Arrange
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUserShouldThrowUserNotFoundException() {

        // Arrange
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(false);

        // Act
        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(id));

        // Assert
        verify(userRepository).existsById(id);
        verify(userRepository, never()).deleteById(any());
    }


}
