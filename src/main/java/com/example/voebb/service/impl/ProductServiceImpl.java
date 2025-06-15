package com.example.voebb.service.impl;

import com.example.voebb.exception.ProductDeletionException;
import com.example.voebb.exception.ProductNotFoundException;
import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.dto.product.*;
import com.example.voebb.model.entity.*;
import com.example.voebb.model.mapper.ProductMapper;
import com.example.voebb.repository.*;
import com.example.voebb.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductItemService productItemService;
    private final BookDetailsService bookDetailsService;
    private final CreatorService creatorService;
    private final CountryRepo countryRepo;
    private final CountryService countryService;
    private final LanguageService languageService;
    private final ProductTypeRepo productTypeRepo;
    private final ProductItemRepo productItemRepo;


    @Override
    @Transactional
    public void createProduct(CreateProductDTO dto) {
        if (dto.getProductTypeId() == null) {
            throw new IllegalArgumentException("Product type is required.");
        }

        ProductType productType = productTypeRepo.findById(dto.getProductTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product type ID: " + dto.getProductTypeId()));

        List<Country> countries = countryService.getCountriesByIds(dto.getCountryIds());
        List<Language> languages = languageService.getLanguagesByIds(dto.getLanguageIds());

        Product newProduct = new Product();
        newProduct.setType(productType);
        newProduct.setTitle(dto.getTitle());
        newProduct.setReleaseYear(dto.getReleaseYear());
        newProduct.setPhoto(dto.getPhoto());
        newProduct.setDescription(dto.getDescription());
        newProduct.setProductLinkToEmedia(dto.getProductLinkToEmedia());
        newProduct.setCountries(countries);
        newProduct.setLanguages(languages);

        if (dto.getCreators() == null || dto.getCreators().isEmpty()) {
            throw new IllegalArgumentException("At least one creator is required.");
        }

        List<CreatorWithRoleDTO> validCreators = dto.getCreators().stream()
                .filter(creator -> creator != null &&
                                   creator.getLastName() != null && !creator.getLastName().isBlank() &&
                                   creator.getRole() != null && !creator.getRole().isBlank())
                .toList();

        if (validCreators.isEmpty()) {
            throw new IllegalArgumentException("At least one valid creator with role is required.");
        }

        creatorService.assignCreatorsToProduct(validCreators, newProduct);

        productRepo.save(newProduct);

        if (newProduct.isBook() && dto.getBookDetails() != null) {
            bookDetailsService.saveBookDetails(dto.getBookDetails(), newProduct);
        }

    }


    @Override
    public Page<CardProductDTO> getProductCardsByFilters(ProductFilters filters, Pageable pageable) {
        Page<Product> page = productRepo.searchWithFilters(
                null,
                filters.getTitle(),
                filters.getAuthor(),
                filters.getLibraryId(),
                filters.getProductType(),
                filters.getLanguageId(),
                filters.getCountryId(),
                pageable);

        return page.map(product -> new CardProductDTO(
                product.getId(),
                product.getType().getName(),
                product.getTitle(),
                product.getReleaseYear(),
                product.getPhoto(),
                product.getType().getDefaultCoverUrl(),
                product.getProductLinkToEmedia(),
                product.getCreatorProductRelations().stream()
                        .filter(relation -> relation.getCreatorRole().getId().equals(product.getType().getMainCreatorRoleId()))
                        .map(relation -> relation.getCreator().getFirstName() + " " + relation.getCreator().getLastName())
                        .collect(Collectors.joining(", ")),
                productItemService.getLocationsForAvailableItemsByProductId(product.getId())));
    }


    @Override
    public Page<ProductInfoDTO> getAllByTitleAdmin(String title, Pageable pageable) {
        Page<Product> page = productRepo.searchWithFilters(null, title, null, null, null, null, null, pageable);
        return page.map(ProductMapper::toProductInfoDTO);
    }

    @Override
    public ProductInfoDTO getProductInfoDTOById(Long id) {
        Product product = productRepo.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.toProductInfoDTO(product);
    }

    @Override
    @Transactional
    public UpdateProductDTO updateProduct(Long productId, UpdateProductDTO updateProductDTO) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setTitle(updateProductDTO.getTitle());
        product.setReleaseYear(updateProductDTO.getReleaseYear());
        product.setDescription(updateProductDTO.getDescription());
        product.setPhoto(updateProductDTO.getPhoto());
        product.setProductLinkToEmedia(updateProductDTO.getProductLinkToEmedia());

        product.setLanguages(languageService.getLanguagesByIds(updateProductDTO.getLanguageIds()));
        product.setCountries(countryRepo.findAllById(updateProductDTO.getCountryIds()));

        creatorService.updateCreatorsForProduct(product, updateProductDTO.getCreators());

        if (product.isBook()) {
            bookDetailsService.updateDetails(product, updateProductDTO.getBookDetails());
        }

        return updateProductDTO;
    }


    @Override
    @Transactional
    public UpdateProductDTO getUpdateProductDTOById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductMapper.toUpdateProductDTO(product);
    }

    @Override
    public Page<GetProductAdminDTO> getFilteredProductsAdmin(ProductFilters filters, Pageable pageable) {

        Page<Product> page = productRepo.searchWithFilters(
                filters.getProductId(),
                filters.getTitle(),
                null,
                null,
                filters.getProductType(),
                null,
                null,
                pageable);

        return page.map(product -> {
            String mainCreator = product.getCreatorProductRelations().stream()
                    .filter(relation -> relation.getCreatorRole().getId().equals(product.getType().getMainCreatorRoleId()))
                    .map(relation -> relation.getCreator().getFirstName() + " " + relation.getCreator().getLastName())
                    .collect(Collectors.joining(", "));

            if (mainCreator.isBlank()) {
                mainCreator = "N/A";
            }

            String emediaLink = product.getProductLinkToEmedia();
            if (emediaLink == null || emediaLink.isBlank()) {
                emediaLink = "N/A";
            }

            return new GetProductAdminDTO(
                    product.getId(),
                    product.getTitle(),
                    product.getType().getName(),
                    mainCreator,
                    emediaLink
            );
        });
    }

    @Transactional
    @Override
    public void deleteProductById(Long productId) {

        if (!productRepo.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        long copyCount = productItemRepo.countByProductId(productId);
        if (copyCount > 0) {
            throw new ProductDeletionException(productId, copyCount);
        }
        productRepo.deleteById(productId);
    }
}
