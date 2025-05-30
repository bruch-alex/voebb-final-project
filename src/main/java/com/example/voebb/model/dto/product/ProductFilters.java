package com.example.voebb.model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters {
    // Product filters
    private String title;
    private String author;
    private Long languageId;
    private Long countryId;
    // Book filters
    private Long libraryId;
    private Long productType;
}
