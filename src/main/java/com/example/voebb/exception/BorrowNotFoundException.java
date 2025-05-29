package com.example.voebb.exception;

public class BorrowNotFoundException extends RuntimeException {

    public BorrowNotFoundException(Long id) {
        super("Borrow record with [ID: #" + id + "] was not found.");
    }

    public BorrowNotFoundException(String message) {
        super(message);
    }
}
