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
        String description,
        String productLinkToEmedia,
        Set<String> countries,
        Set<String> languages,
        BookDetailsDTO bookDetails,
        List<CreatorWithRoleDTO> creators
) {
}
