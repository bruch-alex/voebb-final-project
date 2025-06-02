package com.example.voebb.controller.web;

import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.model.dto.product.CardProductDTO;
import com.example.voebb.model.dto.product.ProductFilters;
import com.example.voebb.model.dto.product.ProductInfoDTO;
import com.example.voebb.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductControllerWeb {
    private final ProductService productService;
    private final BookDetailsService bookDetailsService;
    private final ProductItemService productItemService;
    private final CreatorService creatorService;
    private final LibraryService libraryService;


    @GetMapping
    public String getIndexPage(@ModelAttribute(name = "libraries") List<LibraryDTO> libraries) {
        return "public/index";
    }

    @PostMapping("/search")
    public String postSearchResultPage(@ModelAttribute ProductFilters productFilters,
                                       RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("productFilters", productFilters);
        return "redirect:/search?page=1";
    }

    @GetMapping("/search")
    public String getSearchResultPage(@ModelAttribute(name = "productFilters") ProductFilters productFilters,
                                      @RequestParam(defaultValue = "1") int page,
                                      Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<CardProductDTO> resultProducts = productService.getProductCardsByFilters(productFilters, pageable);
        model.addAttribute("page", resultProducts);
        model.addAttribute("cardProductDTOs", resultProducts.getContent());
        return "/public/product/product-list";
    }


    @GetMapping("/products/{id}")
    public String getDetailsPage(@PathVariable Long id,
                                 Model model) {
        ProductInfoDTO productInfoDTO = productService.getProductInfoDTOById(id);

        model.addAttribute("productInfoDTO", productInfoDTO);
        model.addAttribute("bookDetailsDTO", bookDetailsService.getDetailsDTOByProductId(id));
        model.addAttribute("locationAndItemStatusDTOs", productItemService.getAllLocationsForProduct(id));
        return "public/product/product-full-details";
    }
}
