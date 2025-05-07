package com.example.voebb.exception;

public class CreatorNotFoundException extends RuntimeException {
    public CreatorNotFoundException(Long id) {
        super("Creator with ID " + id + " not found");
    }
}
