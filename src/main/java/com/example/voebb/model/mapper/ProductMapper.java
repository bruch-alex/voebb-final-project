package com.example.voebb.model.mapper;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.dto.creator.UpdateCreatorWithRoleDTO;
import com.example.voebb.model.dto.product.ProductInfoDTO;
import com.example.voebb.model.dto.product.UpdateProductDTO;
import com.example.voebb.model.entity.Country;
import com.example.voebb.model.entity.Language;
import com.example.voebb.model.entity.Product;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductInfoDTO toProductInfoDTO(Product product) {
        Set<String> countryNames = product.getCountries()
                .stream()
                .map(Country::getName)
                .collect(Collectors.toSet());

        Set<String> languageNames = product.getLanguages()
                .stream()
                .map(Language::getName)
                .collect(Collectors.toSet());

        return new ProductInfoDTO(
                product.getId(),
                product.getType().getName(),
                product.getTitle(),
                product.getReleaseYear(),
                product.getPhoto(),
                product.getType().getDefaultCoverUrl(),
                product.getDescription(),
                product.getProductLinkToEmedia(),
                countryNames,
                languageNames,
                product.isBook() ? BookDetailsMapper.toDto(product.getBookDetails()) : null,
                product.getCreatorProductRelations().stream()
                        .map(relation -> new CreatorWithRoleDTO(
                                relation.getCreator().getFirstName(),
                                relation.getCreator().getLastName(),
                                relation.getCreatorRole().getCreatorRoleName()
                        )).toList()
        );
    }

    public static UpdateProductDTO toUpdateProductDTO(Product product) {
        UpdateProductDTO dto = new UpdateProductDTO();
        dto.setId(product.getId());
        dto.setProductTypeId(product.getType().getId());
        dto.setTitle(product.getTitle());
        dto.setReleaseYear(product.getReleaseYear());
        dto.setPhoto(product.getPhoto());
        dto.setDescription(product.getDescription());
        dto.setProductLinkToEmedia(product.getProductLinkToEmedia());

        if (product.isBook()) {
            dto.setBookDetails(BookDetailsMapper.toDto(product.getBookDetails()));
        }

        dto.setCreators(product.getCreatorProductRelations().stream()
                .map(relation -> new UpdateCreatorWithRoleDTO(
                        relation.getCreator().getId(),
                        relation.getCreator().getFirstName(),
                        relation.getCreator().getLastName(),
                        relation.getCreatorRole().getId()
                ))
                .toList());

        dto.setCountryIds(product.getCountries().stream()
                .map(Country::getId)
                .toList());

        dto.setLanguageIds(product.getLanguages().stream()
                .map(Language::getId)
                .toList());

        return dto;
    }


}
