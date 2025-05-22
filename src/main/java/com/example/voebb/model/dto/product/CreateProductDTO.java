package com.example.voebb.model.dto.product;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {

    @NotNull(message = "Product type is required")
    private Long productTypeId;

    @NotBlank(message = "Title is required")
    private String title;

    private String releaseYear;

    private String photo;

    private String description;

    private String productLinkToEmedia;

    private BookDetailsDTO bookDetails;

    @NotEmpty(message = "At least one country must be selected")
    private List<Long> countryIds;

    @NotEmpty(message = "At least one language must be selected")
    private List<Long> languageIds;

    @Size(min = 1, message = "At least one creator must be added")
    @Valid
    private List<@Valid CreatorWithRoleDTO> creators;
}
