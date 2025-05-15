package com.example.voebb.model.dto.product;

import java.util.List;

public record UpdateProductDTO(
        Long id,
        String productType,
        String title,
        String releaseYear,
        String photo,
        String description,
        String productLinkToEmedia,

        BookDetailsDTO bookDetails,   // nullable for non-books
        // TODO: Add creators
        List<Long> countryIds,

        List<Long> languageIds
) {
}
