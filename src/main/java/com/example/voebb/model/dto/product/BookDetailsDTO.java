package com.example.voebb.model.dto.product;

public record BookDetailsDTO(
        String isbn,
        String edition,
        Integer pages
) {
    public BookDetailsDTO() {
        this(null, null, null);
    }
}
