package com.example.voebb.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super("Item [ID: #" + id + "] was not found.");
    }
}
