package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.dto.product.CardProductDTO;
import com.example.voebb.model.dto.product.CreateProductDTO;
import com.example.voebb.model.dto.product.ProductInfoDTO;
import com.example.voebb.model.dto.product.UpdateProductDTO;
import com.example.voebb.model.entity.Country;
import com.example.voebb.model.entity.Language;
import com.example.voebb.model.entity.Product;
import com.example.voebb.model.entity.ProductType;
import com.example.voebb.model.mapper.ProductMapper;
import com.example.voebb.repository.CountryRepo;
import com.example.voebb.repository.ProductRepo;
import com.example.voebb.repository.ProductTypeRepo;
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

        productRepo.save(newProduct);

        if (newProduct.isBook() && dto.getBookDetails() != null) {
            bookDetailsService.saveBookDetails(dto.getBookDetails(), newProduct);
        }

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
    }


    @Override
    public Page<CardProductDTO> getProductCardsByTitle(String title, Pageable pageable) {
        Page<Product> page = productRepo.findAllByTitleContainsIgnoreCase(title, pageable);

        return page.map(product -> new CardProductDTO(
                product.getId(),
                product.getType().getName(),
                product.getTitle(),
                product.getReleaseYear(),
                product.getPhoto(),
                product.getProductLinkToEmedia(),
                product.getCreatorProductRelations().stream()
                        .filter(relation -> relation.getCreatorRole().getId().equals(product.getType().getMainCreatorRoleId()))
                        .map(relation -> relation.getCreator().getFirstName() + " " + relation.getCreator().getLastName())
                        .collect(Collectors.joining(", ")),
                productItemService.getLocationsForAvailableItemsByProductId(product.getId())));
    }

    @Override
    public Page<ProductInfoDTO> getAllByTitleAdmin(String title, Pageable pageable) {
        Page<Product> page = productRepo.findAllByTitleContainsIgnoreCase(title, pageable);
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
    public UpdateProductDTO getUpdateProductDTOById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductMapper.toUpdateProductDTO(product);
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    @Override
    @Transactional
    public UpdateProductDTO updateProduct(Long productId, UpdateProductDTO updateProductDTO) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Country> countries = countryRepo.findAllById(updateProductDTO.countryIds());

        if (existingProduct.isBook()) {
            bookDetailsService.updateDetails(existingProduct, updateProductDTO.bookDetails());
        }

        existingProduct.setTitle(updateProductDTO.title());
        existingProduct.setReleaseYear(updateProductDTO.releaseYear());
        existingProduct.setDescription(updateProductDTO.description());
        existingProduct.setPhoto(updateProductDTO.photo());
        existingProduct.setProductLinkToEmedia(updateProductDTO.productLinkToEmedia());
        existingProduct.setCountries(countries);
        productRepo.save(existingProduct);
        return updateProductDTO;
    }

    @Override
    public void deleteProductById(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepo.deleteById(productId);
    }

}
