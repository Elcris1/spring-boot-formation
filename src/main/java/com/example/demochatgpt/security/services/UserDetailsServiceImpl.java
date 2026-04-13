package com.example.demochatgpt.security.services;

import com.example.demochatgpt.exceptions.UserNotFoundException;
import com.example.demochatgpt.services.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService service) {
        this.userService = service;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, UserNotFoundException {
        var usr = userService.getUserByEmail(email);
        var roles_list = usr.getRoles().stream().map(
                role -> new SimpleGrantedAuthority( role.getName())
        ).toList();


        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("{noop}password")
                .authorities(roles_list)
                .build();
    }
}
