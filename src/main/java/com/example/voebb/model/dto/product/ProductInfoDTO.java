package com.example.voebb.model.dto.product;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;

import java.util.List;
import java.util.Set;

/**
    DTO with all info from Product
 */
public record ProductInfoDTO(
        Long id,
        String productType,
        String title,
        String releaseYear,
        String photo,
        String defaultPhoto,
        String description, // summary of the product based on media_type
        String productLinkToEmedia,
        Set<String> countries,
        BookDetailsDTO bookDetails, //  null for non-books
        List<CreatorWithRoleDTO> creators
        //TODO: add Languages
) {
}
