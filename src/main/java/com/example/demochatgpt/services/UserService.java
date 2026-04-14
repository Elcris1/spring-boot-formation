package com.example.demochatgpt.services;

import java.util.List;
import java.util.Set;

import com.example.demochatgpt.dto.*;
import com.example.demochatgpt.exceptions.InvalidFieldsException;
import com.example.demochatgpt.exceptions.UserAlreadyExistsException;
import com.example.demochatgpt.mapper.UserMapper;
import com.example.demochatgpt.models.Role;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.example.demochatgpt.exceptions.UserNotFoundException;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    //private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    //Rest METHODS
    public List<UserResponseDTO> getUsers() {
        return  userMapper.toDtoList(userRepository.findAll());
    }
    public List<DetailedUserResponseDTO> getDetailedUsers() {
        return userMapper.toDetailedDtoList(userRepository.findAll());
    }

    public UserResponseDTO getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
            .orElseThrow( () -> new UserNotFoundException()));
        
    }

    public DetailedUserResponseDTO getDetailedUserById(Long id) {
        return userMapper.toDetailedDto(userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException()));
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public User createUser(UserCreateRequestDTO rq) {
        if (userRepository.findByEmail(rq.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        try {
            user.setEmail(rq.getEmail());
            user.setPassword(rq.getPassword());
            Role role = roleService.getRoleByName("USER");
            user.setRoles(Set.of(role));
            return userRepository.save(user);
        }   catch (ConstraintViolationException ex) {
            throw new InvalidFieldsException("User fields are not valid");
        }
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

    //AUTH methods
    public User validateUser(UserLoginDTO rq) {
        var user = userRepository.findByEmail(rq.getEmail()).orElseThrow(UserNotFoundException::new);

        if (user.getPassword().equals(rq.getPassword())) {
            return user;
        }

        throw new BadCredentialsException("Credentials not valid");
    }

    //ROle things
    public void addRoles(Long id, AddRolesRequestDTO roles) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        var userRoles = user.getRoles();
        roles.getRoles().forEach( (roleName) -> {
                    var role = roleService.getRoleByName(roleName);
                    userRoles.add(role);
                }
        );
        user.setRoles(userRoles);
        userRepository.save(user);
    }
}
