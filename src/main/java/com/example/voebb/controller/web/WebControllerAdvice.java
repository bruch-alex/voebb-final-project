package com.example.voebb.controller.web;

import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.model.dto.product.ProductFilters;
import com.example.voebb.model.dto.product.ProductTypeDTO;
import com.example.voebb.model.entity.Country;
import com.example.voebb.model.entity.Language;
import com.example.voebb.service.CountryService;
import com.example.voebb.service.LanguageService;
import com.example.voebb.service.LibraryService;
import com.example.voebb.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private final LibraryService libraryService;
    private final ProductTypeService productTypeService;
    private final LanguageService languageService;
    private final CountryService countryService;

    @ModelAttribute("productFilters")
    public ProductFilters productFilters() {
        return new ProductFilters();
    }

    @ModelAttribute("libraries")
    public List<LibraryDTO> libraries() {
        return libraryService.getAllLibraries();
    }

    @ModelAttribute("productTypes")
    public List<ProductTypeDTO> productTypes() {
        return productTypeService.getAllProductTypes();
    }

    @ModelAttribute("productLanguages")
    public List<Language> productLanguages() {
        return languageService.getAllLanguages();
    }

    @ModelAttribute("productCountries")
    public List<Country> productCountries() {
        return countryService.getAllCountries();
    }

}
