package com.example.voebb.exception;

public class UserBorrowLimitExceededException extends RuntimeException {
    public UserBorrowLimitExceededException(Long id, String firstName, String lastName) {
        super("User " + firstName + " " + lastName + " [ID: #" + id + ") already has 5 active borrows or reservations. No further items can be added.");
    }
}
