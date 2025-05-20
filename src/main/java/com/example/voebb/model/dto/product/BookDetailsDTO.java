package com.example.voebb.model.dto.product;

public record BookDetailsDTO(
        String isbn,
        String edition,
        Integer pages
) {
    public BookDetailsDTO() {
        this(null, null, null);
    }

    public String getIsbn() {
        return isbn;
    }
    public String getEdition() {
        return edition;
    }
    public Integer getPages() {
        return pages;
    }
}
