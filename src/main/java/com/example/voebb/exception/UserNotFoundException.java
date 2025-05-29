package com.example.voebb.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User with [ID: #" + id + "] was not found.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
