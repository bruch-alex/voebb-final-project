package com.example.voebb.controller.web;

import com.example.voebb.model.dto.creator.CreatorRoleResponseDTO;
import com.example.voebb.model.dto.item.ItemFilters;
import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.model.dto.product.ProductFilters;
import com.example.voebb.model.dto.product.ProductTypeDTO;
import com.example.voebb.model.entity.Country;
import com.example.voebb.model.entity.ItemStatus;
import com.example.voebb.model.entity.Language;
import com.example.voebb.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private final LibraryService libraryService;
    private final ProductTypeService productTypeService;
    private final LanguageService languageService;
    private final CountryService countryService;
    private final CreatorRoleService creatorRoleService;
    private final ItemStatusService itemStatusService;
    private final CustomUserService customUserService;

    @ModelAttribute("productFilters")
    public ProductFilters productFilters() {
        return new ProductFilters();
    }

    @ModelAttribute("libraries")
    public Map<Long, String> libraries() {
        return libraryService.getAllLibraries().stream()
                .collect(Collectors.toMap(
                        LibraryDTO::id,
                        LibraryDTO::name
                ));
    }

    @ModelAttribute("librariesDistricts")
    public List<String> librariesDistricts() {
        return libraryService.getAllDistricts();
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

    @ModelAttribute("roles")
    public List<CreatorRoleResponseDTO> roles() {
        return creatorRoleService.getAllCreatorRoles();
    }

    @ModelAttribute("itemFilters")
    public ItemFilters itemFilters() {
        return new ItemFilters();
    }

    @ModelAttribute("itemStatuses")
    public List<ItemStatus> statuses() {
        return itemStatusService.getAllStatuses();
    }

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("borrowExpiresSoon")
    @PreAuthorize("isAuthenticated()")
    public Boolean addBorrowWarning(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return customUserService.isBorrowingExpiresSoon(username);
        }
        return null;
    }

}
