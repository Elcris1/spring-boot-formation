package com.example.demochatgpt.services;

import java.util.List;

import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.exceptions.InvalidFieldsException;
import com.example.demochatgpt.exceptions.UserAlreadyExistsException;
import com.example.demochatgpt.mapper.UserMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;

import com.example.demochatgpt.exceptions.UserNotFoundException;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    //private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getUsers() {
        return  userMapper.toDtoList(userRepository.findAll());
    }

    public UserResponseDTO getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
            .orElseThrow( () -> new UserNotFoundException()));
        
    }

    public User createUser(UserCreateRequestDTO rq) {
        if (userRepository.findByEmail(rq.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        try {
            user.setEmail(rq.getEmail());
            user.setPassword(rq.getPassword());
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

}
