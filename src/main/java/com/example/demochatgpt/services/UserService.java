package com.example.demochatgpt.services;

import java.util.List;

import com.example.demochatgpt.dto.DetailedUserResponseDTO;
import com.example.demochatgpt.dto.UserCreateRequestDTO;
import com.example.demochatgpt.dto.UserLoginDTO;
import com.example.demochatgpt.dto.UserResponseDTO;
import com.example.demochatgpt.exceptions.CredentialsNotValidException;
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

    //AUTH methods
    public UserResponseDTO validateUser(UserLoginDTO rq) {
        var user = userRepository.findByEmail(rq.getEmail());
        if(user.isEmpty()) {
            throw new UserNotFoundException();
        }

        if (user.get().getPassword().equals(rq.getPassword())) {
            return userMapper.toDto(user.get());
        }

        throw new CredentialsNotValidException();
    }
}
