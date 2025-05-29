package com.example.voebb.exception;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(String title, Long id, String status) {
        super("Item '" + title.toUpperCase() + "' [ID: #" + id + ") is not available. Current status: " + status.toUpperCase() + ".");
    }
}
