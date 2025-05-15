package com.example.voebb.controller.web;

import com.example.voebb.model.dto.product.ProductInfoDTO;
import com.example.voebb.model.dto.product.SearchResultProductDTO;
import com.example.voebb.service.BookDetailsService;
import com.example.voebb.service.CreatorProductRelationService;
import com.example.voebb.service.ProductItemService;
import com.example.voebb.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductControllerWeb {
    private final ProductService productService;
    private final CreatorProductRelationService creatorService;
    private final BookDetailsService bookDetailsService;
    private final ProductItemService productItemService;

    public ProductControllerWeb(ProductService productService, CreatorProductRelationService creatorService, BookDetailsService bookDetailsService, ProductItemService productItemService) {
        this.productService = productService;
        this.creatorService = creatorService;
        this.bookDetailsService = bookDetailsService;
        this.productItemService = productItemService;
    }

    @GetMapping
    public String getIndexPage() {
        return "public/index";
    }

    @GetMapping("/search")
    public String getSearchResultPage(@PageableDefault(size = 5) Pageable pageable,
                                      @RequestParam String title,
                                      Model model) {
        Page<SearchResultProductDTO> resultProducts = productService.getAllByTitle(title, pageable);
        model.addAttribute("title", title);
        model.addAttribute("page", resultProducts);
        model.addAttribute("productDTOs", resultProducts.getContent());
        return "public/product/product-list";
    }

    @GetMapping("/products/{id}")
    public String getDetailsPage(@PathVariable Long id,
                                 Model model) {
        ProductInfoDTO productInfoDTO = productService.findById(id);

        model.addAttribute("productInfo", productInfoDTO);
        model.addAttribute("creators", creatorService.getCreatorsByProductId(id));
        model.addAttribute("bookDetails", bookDetailsService.getDetailsDTOByProductId(id));
        model.addAttribute("locations", productItemService.getAllLocationsForProduct(id));
        return "public/product/product-full-details";
    }
}
