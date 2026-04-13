package com.example.demochatgpt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Getter @Setter
    private long id;

    @Column(name = "name", nullable = true, length = 30)
    @Getter @Setter
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Email(message = "Email not valid")
    @Getter @Setter
    private String email;

    @Column(name = "password")
    @Getter @Setter
    private String password;

    @Column(name =  "created_at")
    @Getter @Setter
    private Long createdAt;

    @PrePersist
    private void saveCreatedAt() {
        createdAt = System.currentTimeMillis();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Getter @Setter
    private Set<Role> roles;
}
