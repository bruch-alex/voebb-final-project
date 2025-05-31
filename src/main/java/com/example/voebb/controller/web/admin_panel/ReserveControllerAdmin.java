package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.reservation.CreateReservationDTO;
import com.example.voebb.model.dto.reservation.GetReservationDTO;
import com.example.voebb.service.LibraryService;
import com.example.voebb.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("admin/reservations")
@RequiredArgsConstructor
public class ReserveControllerAdmin {

    private final ReservationService reservationService;
    private final LibraryService libraryService;

    @GetMapping
    public String getReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long libraryId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<GetReservationDTO> page = reservationService.getFilteredReservations(userId, itemId, libraryId, pageable);

        model.addAttribute("page", page);
        model.addAttribute("reservations", page.getContent());
        model.addAttribute("userId", userId);
        model.addAttribute("itemId", itemId);
        model.addAttribute("libraryId", libraryId);
        model.addAttribute("libraries", libraryService.getAllLibraries());

        return "admin/reservations/reservation-content";
    }


    @PostMapping
    public String reserveItem(@ModelAttribute CreateReservationDTO dto,
                              RedirectAttributes redirectAttributes) {
        try {
            reservationService.createReservation(dto);
            redirectAttributes.addFlashAttribute("success", "Reservation created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/reservations";
    }

    @PostMapping("/fulfill/{id}")
    public String fulfillReservation(@PathVariable("id") Long reservationId, RedirectAttributes redirectAttributes) {

        try {
            String message = reservationService.fulfillReservation(reservationId);
            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reservations";
    }


    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.deleteReservation(id);
            redirectAttributes.addFlashAttribute("success", "Reservation deleted successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Reservation not found.");
        }

        return "redirect:/admin/reservations";
    }

}