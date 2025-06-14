package com.example.voebb.controller.web;

import com.example.voebb.model.dto.CarouselEvent;
import com.example.voebb.model.dto.product.CardProductDTO;
import com.example.voebb.model.dto.product.ProductFilters;
import com.example.voebb.model.dto.product.ProductInfoDTO;
import com.example.voebb.model.dto.reservation.CreateReservationDTO;
import com.example.voebb.model.dto.user.UserDTO;
import com.example.voebb.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductControllerWeb {
    private final ProductService productService;
    private final BookDetailsService bookDetailsService;
    private final ProductItemService productItemService;
    private final ReservationService reservationService;
    private final CustomUserService customUserService;


    @GetMapping
    public String getIndexPage(Model model) {

        List<CarouselEvent> events = List.of(
                new CarouselEvent("Reading & Discussions", "Join fellow book lovers for lively conversations around hand-picked titles. Share your thoughts, explore new perspectives, and deepen your love for literature in a relaxed, inclusive setting.", "/images/image1.jpg", "email@example.com"),
                new CarouselEvent("Writing Workshop", "Unleash your inner author! Our hands-on writing sessions guide you through storytelling, structure, and style — whether you're a beginner or brushing up your skills.", "/images/image2.jpg", "email@example.com"),
                new CarouselEvent("Lecture: My money can do more!", "Discover how to make smart financial choices and align your spending with your values. This talk offers tips on ethical banking, sustainable investing, and everyday saving.", "/images/image3.jpg", "email@example.com"),
                new CarouselEvent("Event: Library talks on Security", "From digital privacy to public safety — join experts as we explore how to stay informed and protected in an ever-changing world. Interactive Q&A included.", "/images/image1.jpg", "email@example.com"),
                new CarouselEvent("Poetry Classes for Kids", "Let the words rhyme and rhythm flow! These playful sessions help young poets express themselves creatively while building confidence with language and performance.", "/images/image2.jpg" , "email@example.com" ),
                new CarouselEvent("Electronic Music to join in", "Get hands-on with beats, loops, and sound design. This interactive music lab is open to curious minds of all levels — no experience needed, just your ears and enthusiasm!", "/images/image3.jpg", "email@example.com" ),
                new CarouselEvent("Picture Book Cinema", "Where stories come alive on screen! Cozy up with the little ones for animated adaptations of beloved children’s books — followed by fun discussions and activities.", "/images/image1.jpg","email@example.com" ),
                new CarouselEvent("DisInformation", "Learn to spot fake news and navigate the online world critically. This event explores the mechanics of misinformation and gives you tools to stay media-savvy.", "/images/image2.jpg", "email@example.com" ),
                new CarouselEvent("Events in June", "June is packed with discovery! From literature and learning to music and media, explore a month of diverse events designed to spark curiosity and connect our community.", "/images/image3.jpg", "email@example.com" )


        );
        model.addAttribute("carouselEvents", events);
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
        return "public/product/product-list";
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

    @PostMapping("/reserve/{id}")
    public String reserveItem(@PathVariable("id") Long id,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {

        UserDTO userDTO = customUserService.getUserDTOByUsername(principal.getName());
        CreateReservationDTO dto = new CreateReservationDTO(userDTO.id(), id);

        try {
            reservationService.createReservation(dto);
            redirectAttributes.addFlashAttribute("success", "Reservation created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile#itemActivity";
    }
}
