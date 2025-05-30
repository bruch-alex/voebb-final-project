package com.example.voebb.service;

import com.example.voebb.model.dto.product.*;
import com.example.voebb.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    void createProduct(CreateProductDTO dto);

    Page<CardProductDTO> getProductCardsByFilters(ProductFilters filters, Pageable pageable);

    Page<ProductInfoDTO> getAllByTitleAdmin(String title, Pageable pageable);

    UpdateProductDTO getUpdateProductDTOById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    ProductInfoDTO getProductInfoDTOById(Long id);

    UpdateProductDTO updateProduct(Long productId, UpdateProductDTO updateProductDTO);

    void deleteProductById(Long id);
}
