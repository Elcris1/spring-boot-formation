package com.example.demochatgpt.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Getter @Setter
    private long id;


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

}
