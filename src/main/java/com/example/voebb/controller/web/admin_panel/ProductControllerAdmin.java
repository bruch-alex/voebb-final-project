package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.product.CreateProductDTO;
import com.example.voebb.model.dto.product.GetProductAdminDTO;
import com.example.voebb.model.dto.product.ProductFilters;
import com.example.voebb.model.dto.product.UpdateProductDTO;
import com.example.voebb.model.entity.CreatorRole;
import com.example.voebb.repository.CreatorRoleRepo;
import com.example.voebb.service.*;
import jakarta.persistence.EntityNotFoundException;
import com.example.voebb.model.dto.product.ProductTypeDTO;

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

import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductControllerAdmin {

    private final ProductService productService;
    private final CreatorRoleRepo creatorRoleRepo;
    private final ProductTypeService productTypeService;

    @GetMapping
    public String getAllProducts(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model,
                                 @ModelAttribute ProductFilters productFilters,
                                 @RequestParam(value = "success", required = false) String success) {

        Page<GetProductAdminDTO> page = productService.getFilteredProductsAdmin(productFilters, pageable);
        model.addAttribute("page", page);
        model.addAttribute("products", page.getContent());

        if (success != null && !success.isBlank()) {
            model.addAttribute("success", success);
        }

        return "admin/products/product-list";
    }

    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("createProductDTO", new CreateProductDTO());
        return "admin/products/add-product";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("createProductDTO") CreateProductDTO dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/products/add-product";
        }

        productService.createProduct(dto);
        redirectAttributes.addFlashAttribute("success", "Product created successfully");
        return "redirect:/admin/products";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UpdateProductDTO updateProductDTO = productService.getUpdateProductDTOById(id);
        model.addAttribute("updateProductDTO", updateProductDTO);

        Map<Long, String> roleMap = creatorRoleRepo.findAll().stream()
                .collect(Collectors.toMap(
                        CreatorRole::getId,
                        CreatorRole::getCreatorRoleName
                ));
        model.addAttribute("roleMap", roleMap);

        Map<Long, String> productTypesMap = productTypeService.getAllProductTypes().stream()
                .collect(Collectors.toMap(ProductTypeDTO::getId, ProductTypeDTO::getDisplayName));

        model.addAttribute("productTypesMap", productTypesMap);



        return "admin/products/edit-product";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id,
                                @ModelAttribute("updateProductDTO") UpdateProductDTO updatedProduct,
                                RedirectAttributes redirectAttributes) {


        productService.updateProduct(id, updatedProduct);
        redirectAttributes.addFlashAttribute("success", "Product updated successfully.");
        return "redirect:/admin/products";

    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        try {
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("success", "Product deleted successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (
                EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
        }
        return "redirect:/admin/products";
    }
}
