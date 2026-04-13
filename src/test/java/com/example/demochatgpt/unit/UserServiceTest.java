package com.example.demochatgpt.unit;

import com.example.demochatgpt.dto.*;
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
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashSet;
import java.util.List;
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
    void getUsers_shouldReturnMappedUserList() {
        // Arrange
        List<User> users = List.of(new User(), new User());

        List<UserResponseDTO> dtos = List.of(
                new UserResponseDTO(),
                new UserResponseDTO()
        );

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(dtos);

        // Act
        List<UserResponseDTO> result = userService.getUsers();

        // Assert
        assertEquals(dtos, result);

        verify(userRepository).findAll();
        verify(userMapper).toDtoList(users);
    }

    @Test
    void getDetailedUsers_shouldReturnMappedDetailedUsers() {
        // Arrange
        List<User> users = List.of(new User(), new User());

        List<DetailedUserResponseDTO> dtos = List.of(
                new DetailedUserResponseDTO(),
                new DetailedUserResponseDTO()
        );

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDetailedDtoList(users)).thenReturn(dtos);

        // Act
        List<DetailedUserResponseDTO> result = userService.getDetailedUsers();

        // Assert
        assertEquals(dtos, result);

        verify(userRepository).findAll();
        verify(userMapper).toDetailedDtoList(users);
    }

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

    @Test
    void validateUser_shouldReturnUser_whenCredentialsAreCorrect() {
        // Arrange
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("1234");

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("1234");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        // Act
        User result = userService.validateUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void validateUser_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("notfound@test.com");
        dto.setPassword("1234");

        when(userRepository.findByEmail("notfound@test.com"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.validateUser(dto));
    }

    @Test
    void validateUser_shouldThrowBadCredentialsException_whenPasswordIsWrong() {
        // Arrange
        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("wrong");

        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("correct");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        // Act + Assert
        assertThrows(BadCredentialsException.class,
                () -> userService.validateUser(dto));
    }

    @Test
    void addRoles_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        AddRolesRequestDTO dto = new AddRolesRequestDTO();
        dto.setRoles(List.of("ADMIN", "USER"));

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.addRoles(userId, dto));
    }

    @Test
    void addRoles_shouldAddRoles_andSaveUser_whenUserExists() {
        // Arrange
        Long userId = 1L;

        AddRolesRequestDTO dto = new AddRolesRequestDTO();
        dto.setRoles(List.of("ADMIN", "USER"));

        User user = new User();
        user.setId(userId);
        user.setRoles(new HashSet<>());

        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setName("USER");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(roleService.getRoleByName("ADMIN"))
                .thenReturn(adminRole);

        when(roleService.getRoleByName("USER"))
                .thenReturn(userRole);

        // Act
        userService.addRoles(userId, dto);

        // Assert
        assertTrue(user.getRoles().contains(adminRole));
        assertTrue(user.getRoles().contains(userRole));

        verify(userRepository).save(user);
    }

    @Test
    void addRoles_shouldCallRoleServiceForEachRole() {
        // Arrange
        Long userId = 1L;

        AddRolesRequestDTO dto = new AddRolesRequestDTO();
        dto.setRoles(List.of("ADMIN"));

        User user = new User();
        user.setRoles(new HashSet<>());

        Role role = new Role();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(roleService.getRoleByName("ADMIN"))
                .thenReturn(role);

        // Act
        userService.addRoles(userId, dto);

        // Assert
        verify(roleService).getRoleByName("ADMIN");
        verify(userRepository).save(user);
    }


}
