package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.product.BookDetailsDTO;
import com.example.voebb.model.dto.product.NewProductDTO;
import com.example.voebb.model.dto.product.UpdateProductDTO;
import com.example.voebb.model.entity.Product;
import com.example.voebb.service.CountryService;
import com.example.voebb.service.LanguageService;
import com.example.voebb.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final ProductService productService;
    private final CountryService countryService;
    private final LanguageService languageService;

    // GET: List all products
    @GetMapping
    public String page(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model,
                       @RequestParam(value = "success", required = false) String success) {

        Page<Product> page = productService.getAllProducts(pageable);
        model.addAttribute("page", page);
        model.addAttribute("products", page.getContent());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("languages", languageService.findAll());
        model.addAttribute("productDTO", new NewProductDTO());

        if (success != null && !success.isBlank()) {
            model.addAttribute("success", success);
        }

        return "admin/products/page";
    }

    // POST: Create a new product
    @PostMapping
    public String create(@ModelAttribute("productDTO") NewProductDTO requestDTO,
                         RedirectAttributes ra) {
        List<Long> countryIds = requestDTO.getCountryIds();

        // Logic to process the selected countries along with product creation
        productService.createProduct(requestDTO);
        ra.addFlashAttribute("success", "Product created successfully");
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {

        UpdateProductDTO product = productService.getProductById(id);

        model.addAttribute("product", product);
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("languages", languageService.findAll());
        return "admin/products/edit"; // this should point to the Thymeleaf template for editing
    }

    // Update product - POST method
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("product") UpdateProductDTO updatedProduct,
                                RedirectAttributes ra) {

        // Save the updated product
        productService.updateProduct(id, updatedProduct);
        return "redirect:/admin/products";

    }

    // POST: Delete a product by ID
    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id,
                                RedirectAttributes ra) {
        productService.deleteProductById(id);
        ra.addFlashAttribute("success", "Product deleted successfully");
        return "redirect:/admin/products";
    }

    // TODO: Add EDIT Product functionality


}
