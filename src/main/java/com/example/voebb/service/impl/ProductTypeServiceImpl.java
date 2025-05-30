package com.example.voebb.service.impl;

import com.example.voebb.model.dto.product.ProductTypeDTO;
import com.example.voebb.model.entity.ProductType;
import com.example.voebb.repository.ProductTypeRepo;
import com.example.voebb.service.ProductTypeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepo productTypeRepo;

    @Transactional
    @Override
    public ProductType findOrCreate(String productTypeName) {
        if (productTypeName == null || productTypeName.isBlank()) {
            throw new IllegalArgumentException("ProductTypeName name must be non-empty");
        }

        String sanitizedName = productTypeName.trim();

        return productTypeRepo.findByNameIgnoreCase(sanitizedName)
                .orElseGet(() -> {
                    ProductType pt = new ProductType();
                    pt.setName(sanitizedName);
                    return productTypeRepo.save(pt);
                });
    }

    @Override
    public ProductType findByName(String name) {
        return productTypeRepo.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> new EntityNotFoundException("Product type not found: " + name));
    }


    @Override
    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeRepo.findAll().stream()
                .map(productType -> new ProductTypeDTO(
                        productType.getId(),
                        productType.getName()
                )).toList();
    }

    @Override
    public List<ProductType> getProductTypeByIds(List<Long> ids){
        return productTypeRepo.findAllById(ids);
    }
}
