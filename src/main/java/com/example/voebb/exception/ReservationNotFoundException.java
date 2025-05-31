package com.example.voebb.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Reservation record with [ID: #" + id + "] was not found.");
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
