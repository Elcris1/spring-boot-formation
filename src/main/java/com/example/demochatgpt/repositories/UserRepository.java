package com.example.demochatgpt.repositories;

import com.example.demochatgpt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {

}
