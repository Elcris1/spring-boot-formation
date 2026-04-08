package com.example.demochatgpt.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demochatgpt.exceptions.UserNotFoundException;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return  userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow( () -> new UserNotFoundException());
        
    }

    public User createUser(String email, String password ) {
        if (!userRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(id);
    }

}
