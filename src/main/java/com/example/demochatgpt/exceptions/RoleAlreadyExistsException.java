package com.example.demochatgpt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoleAlreadyExistsException extends RuntimeException{
    public RoleAlreadyExistsException(){
        super("Role already exists!");
    }
}
