package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.CreatorRequestDTO;
import com.example.voebb.model.dto.product.*;
import com.example.voebb.model.entity.Country;
import com.example.voebb.model.entity.Language;
import com.example.voebb.model.entity.Product;
import com.example.voebb.model.entity.ProductType;
import com.example.voebb.repository.CountryRepo;
import com.example.voebb.repository.LanguageRepo;
import com.example.voebb.repository.ProductRepo;
import com.example.voebb.service.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CreatorProductRelationService creatorProductRelationService;
    private final ProductItemService productItemService;
    private final BookDetailsService bookDetailsService;
    private final CreatorService creatorService;
    private final ProductTypeService productTypeService;
    private final CountryRepo countryRepo;
    private final LanguageRepo languageRepo;

    public ProductServiceImpl(
            ProductRepo productRepo,
            BookDetailsService bookDetailsService,
            CreatorProductRelationService creatorProductRelationService,
            CreatorService creatorService,
            ProductTypeService productTypeService,
            CountryRepo countryRepo,
            ProductItemService productItemService, LanguageRepo languageRepo) {
        this.productRepo = productRepo;
        this.bookDetailsService = bookDetailsService;
        this.creatorService = creatorService;
        this.productTypeService = productTypeService;
        this.creatorProductRelationService = creatorProductRelationService;
        this.productItemService = productItemService;
        this.countryRepo = countryRepo;
        this.languageRepo = languageRepo;
    }

    @Override
    public Page<SearchResultProductDTO> getAllByTitle(String title, Pageable pageable) {
        Page<Product> page = productRepo.findAllByTitleContainsIgnoreCase(title, pageable);

        return page.map(product -> {
            CreatorRequestDTO mainCreator = creatorProductRelationService.getCreatorsByProductId(product.getId())
                    .stream()
                    .filter(creator -> creator.roleId() == 1)
                    .findFirst()
                    .map(creatorWithRoleDTO -> new CreatorRequestDTO(
                            creatorWithRoleDTO.firstName(),
                            creatorWithRoleDTO.lastName()
                    ))
                    .orElse(null);

            return new SearchResultProductDTO(
                    product.getId(),
                    product.getType().getName(),
                    product.getTitle(),
                    product.getReleaseYear(),
                    product.getPhoto(),
                    product.getProductLinkToEmedia(),
                    mainCreator,
                    productItemService.getLocationsForAvailableItemsByProductId(product.getId()));
        });
    }

    @Override
    public Page<ProductInfoDTO> getAllByTitleAdmin(String title, Pageable pageable) {
        Page<Product> page = productRepo.findAllByTitleContainsIgnoreCase(title, pageable);
        return page.map(this::toProductInfoDTO);
    }


    @Override
    public ProductInfoDTO findById(Long id) {
        Product product = productRepo.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

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
                product.getDescription(),
                product.getProductLinkToEmedia(),
                countryNames,
                bookDetailsService.getDetailsDTOByProductId(product.getId())
        );
    }

    public AdminProductDTO createProduct(NewProductDTO dto) {

        // 1. Link with existing media_type
        ProductType productType = productTypeService.findOrCreate(dto.getProductType().trim());

        // 2. Save to Product table
        Product savedProduct = buildAndSaveProduct(dto, productType);

        // 3. If 'book' || 'e-book' add book details
        String type = dto.getProductType().trim().toLowerCase();
        boolean isBook = type.equals("book") || type.equals("ebook");

        if (isBook && dto.getBookDetails() != null) {
            bookDetailsService.saveBookDetails(dto.getBookDetails(), savedProduct);
        }

        // 4. Link creator with the product
        if (dto.getCreators() == null || dto.getCreators().isEmpty()) {
            throw new IllegalArgumentException("A product must have at least one creator.");
        }
        creatorService.assignCreatorsToProduct(dto.getCreators(), savedProduct);

        return new AdminProductDTO(
                savedProduct.getId(),
                savedProduct.getTitle(),
                productType.getName());

    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    @Override
    public void deleteProductById(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepo.deleteById(productId);
    }

    @Override
    public UpdateProductDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BookDetailsDTO bookDetailsDTO;

        if (product.getBookDetails() != null) {
            bookDetailsDTO = new BookDetailsDTO(
                    product.getBookDetails().getIsbn(),
                    product.getBookDetails().getEdition(),
                    product.getBookDetails().getPages()
            );
        } else {
            bookDetailsDTO = new BookDetailsDTO("", "", 0);
        }


        return new UpdateProductDTO(
                product.getId(),
                product.getType().getName(),
                product.getTitle(),
                product.getReleaseYear(),
                product.getPhoto(),
                product.getDescription(),
                product.getProductLinkToEmedia(),
                bookDetailsDTO,
                product.getCountries().stream()
                        .map(Country::getId)
                        .toList(),
                product.getLanguages().stream()
                        .map(Language::getId)
                        .toList()
        );
    }

    @Override
    @Transactional
    public UpdateProductDTO updateProduct(Long productId, UpdateProductDTO updateProductDTO) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Country> countries = countryRepo.findAllById(updateProductDTO.countryIds());
        Set<Language> languages = languageRepo.findAllById(updateProductDTO.languageIds())
                .stream()
                .collect(Collectors.toSet());
        // TODO: make mapper
        existingProduct.setTitle(updateProductDTO.title());
        existingProduct.setReleaseYear(updateProductDTO.releaseYear());
        existingProduct.setDescription(updateProductDTO.description());
        existingProduct.setPhoto(updateProductDTO.photo());
        existingProduct.setProductLinkToEmedia(updateProductDTO.productLinkToEmedia());
        existingProduct.setCountries(countries);
        existingProduct.setLanguages(languages);
        BookDetailsDTO bookDetailsDTO = updateProductDTO.bookDetails();
        if (bookDetailsDTO != null) {
            bookDetailsService.updateOrCreateBookDetails(bookDetailsDTO, existingProduct);
        }
        productRepo.save(existingProduct);
        return updateProductDTO;
    }

    @Override
    @Transactional
    public void saveProduct(Product existingProduct) {
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        if (existingProduct.getId() == null) {
            throw new IllegalArgumentException("Product must have an ID");
        }
        if (!productRepo.existsById(existingProduct.getId())) {
            throw new IllegalArgumentException("Product with ID " + existingProduct.getId() + " does not exist");
        }
        productRepo.save(existingProduct);
    }

    // TODO: extract mapper method from here
    private Product buildAndSaveProduct(NewProductDTO dto, ProductType productType) {
        List<Country> countries = countryRepo.findAllById(dto.getCountryIds());

        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setReleaseYear(dto.getReleaseYear());
        product.setPhoto(dto.getPhoto());
        product.setDescription(dto.getDescription());
        product.setProductLinkToEmedia(dto.getProductLinkToEmedia());
        product.setType(productType);
        product.setCountries(countries);
        return productRepo.save(product);
    }

    private ProductInfoDTO toProductInfoDTO(Product product) {
        BookDetailsDTO bookDetailsDTO = bookDetailsService.getDetailsDTOByProductId(product.getId());

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
                product.getDescription(),
                product.getProductLinkToEmedia(),
                countryNames,
                bookDetailsDTO
        );
    }

}
