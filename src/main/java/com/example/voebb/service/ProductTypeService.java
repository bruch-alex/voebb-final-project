package com.example.voebb.service;


import com.example.voebb.model.dto.product.ProductTypeDTO;
import com.example.voebb.model.entity.ProductType;

import java.util.List;

public interface ProductTypeService {

    ProductType findOrCreate(String productTypeName);

    ProductType findByName(String name);

    List<ProductTypeDTO> getAllProductTypes();

    List<ProductType> getProductTypeByIds(List<Long> ids);

}
