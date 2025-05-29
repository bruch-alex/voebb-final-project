package com.example.voebb.exception;

public class BorrowExtensionLimitReachedException extends RuntimeException {

    public BorrowExtensionLimitReachedException(Long borrowId) {
        super("This borrow [ID: #" + borrowId + "] has already been extended the maximum of 2 times.");
    }
}
