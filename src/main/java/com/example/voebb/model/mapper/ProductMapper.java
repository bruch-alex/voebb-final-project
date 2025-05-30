package com.example.voebb.model.mapper;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.dto.product.ProductInfoDTO;
import com.example.voebb.model.dto.product.UpdateProductDTO;
import com.example.voebb.model.entity.Country;
import com.example.voebb.model.entity.Product;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductInfoDTO toProductInfoDTO(Product product) {
        Set<String> countryNames = product.getCountries()
                .stream()
                .map(Country::getName)
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
        return new UpdateProductDTO(
                product.getId(),
                product.getTitle(),
                product.getReleaseYear(),
                product.getPhoto(),
                product.getDescription(),
                product.getProductLinkToEmedia(),
                product.isBook() ? BookDetailsMapper.toDto(product.getBookDetails()) : null,
                product.getCountries().stream().map(
                        Country::getId
                ).toList()
        );

    }
}
