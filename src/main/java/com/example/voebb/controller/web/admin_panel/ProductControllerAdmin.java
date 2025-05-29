package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.product.CreateProductDTO;
import com.example.voebb.model.entity.Product;
import com.example.voebb.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final ProductService productService;
    private final CountryService countryService;
    private final LanguageService languageService;
    private final ProductTypeService productTypeService;
    private final CreatorRoleService creatorRoleService;

    @GetMapping
    public String getAllProducts(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model,
                                 @RequestParam(value = "success", required = false) String success) {

        Page<Product> page = productService.getAllProducts(pageable);
        model.addAttribute("page", page);
        model.addAttribute("products", page.getContent());
        model.addAttribute("countries", countryService.getAllCountries());

        if (success != null && !success.isBlank()) {
            model.addAttribute("success", success);
        }

        return "admin/products/product-list";
    }


    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("createProductDTO", new CreateProductDTO());
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("languages", languageService.getAllLanguages());
        model.addAttribute("productTypes", productTypeService.getAllProductTypes());
        model.addAttribute("roles", creatorRoleService.getAllCreatorRoles());
        return "admin/products/add-product";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("createProductDTO") CreateProductDTO dto,
                                BindingResult bindingResult,
                                RedirectAttributes ra,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", countryService.getAllCountries());
            model.addAttribute("languages", languageService.getAllLanguages());
            model.addAttribute("productTypes", productTypeService.getAllProductTypes());
            model.addAttribute("roles", creatorRoleService.getAllCreatorRoles());
            return "admin/products/add-product";
        }

        productService.createProduct(dto);
        ra.addFlashAttribute("success", "Product created successfully");
        return "redirect:/admin/products";
    }


//
//    @GetMapping("/edit/{id}")
//    public String editProduct(@PathVariable("id") Long id, Model model) {
//        UpdateProductDTO product = productService.getUpdateProductDTOById(id);
//
//        model.addAttribute("updateProductDTO", product);
//        model.addAttribute("countries", countryService.findAll());
//        return "edit-product"; // this should point to the Thymeleaf template for editing
//    }
//
//    @PostMapping("/edit/{id}")
//    public String updateProduct(@PathVariable("id") Long id,
//                                @ModelAttribute("updateProductDTO") UpdateProductDTO updatedProduct,
//                                RedirectAttributes ra) {
//
//        // Save the updated product
//        productService.updateProduct(id, updatedProduct);
//        return "redirect:/admin/products";
//
//    }
//
//    @PostMapping("/deleteProduct/{id}")
//    public String deleteProduct(@PathVariable Long id,
//                                RedirectAttributes ra) {
//        productService.deleteProductById(id);
//        ra.addFlashAttribute("success", "Product deleted successfully");
//        return "redirect:/admin/products";
//    }

}
