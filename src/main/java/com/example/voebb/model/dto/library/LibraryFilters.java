package com.example.voebb.model.dto.library;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LibraryFilters {
    private Long libraryId;
    private String name;
    private String district;
}
