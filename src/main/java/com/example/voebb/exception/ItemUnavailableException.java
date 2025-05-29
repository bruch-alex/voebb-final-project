package com.example.voebb.exception;

public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException(String title, Long id, String status) {
        super("Item: '" + title.toUpperCase() + "' [ID: #" + id + "] is not available. Current status: " + status);
    }
}