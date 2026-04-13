package com.example.demochatgpt.utils;

import com.example.demochatgpt.models.Role;
import com.example.demochatgpt.models.User;
import com.example.demochatgpt.repositories.RoleRepository;
import com.example.demochatgpt.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            var role1 = new Role();
            role1.setName("USER");

            roleRepository.save(role1);

            var role2 = new Role();
            role2.setName("ADMIN");

            roleRepository.save(role2);

            var admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@email.com");
            admin.setPassword("1234");
            admin.setRoles(Set.of(role1, role2));

            userRepository.save(admin);

            var user = new User();
            user.setName("user");
            user.setEmail("user@email.com");
            user.setPassword("1234");
            user.setRoles(Set.of(role1));
            userRepository.save(user);

        };
    }
}
