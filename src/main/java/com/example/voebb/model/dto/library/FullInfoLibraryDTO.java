package com.example.voebb.model.dto.library;

public record FullInfoLibraryDTO(
        Long id,
        String name,
        String description,
        String postcode,
        String city,
        String district,
        String street,
        String houseNumber,
        String osmLink) {
}
