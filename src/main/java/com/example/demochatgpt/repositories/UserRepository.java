package com.example.demochatgpt.repositories;

import com.example.demochatgpt.models.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable page);
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    
}
