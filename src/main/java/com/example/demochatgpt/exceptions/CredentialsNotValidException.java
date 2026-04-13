package com.example.demochatgpt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CredentialsNotValidException extends RuntimeException {
    public CredentialsNotValidException() {
        super("Invalid credentials");
    }
}
