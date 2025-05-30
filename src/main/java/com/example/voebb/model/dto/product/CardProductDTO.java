package com.example.voebb.model.dto.product;

import java.util.List;

/**
 * DTO with all info for ProductCard (SearchResults)
 */
public record CardProductDTO(
        Long id,
        String productType,
        String title,
        String releaseYear,
        String photo,
        String defaultPhoto,
        String productLinkToEmedia,
        String mainCreators,
        List<LocationAndItemsCountDTO> locations) {}