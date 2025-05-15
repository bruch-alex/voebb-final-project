package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.reservation.ReservationResponseDTO;
import com.example.voebb.service.LibraryService;
import com.example.voebb.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("admin/reservations")
@RequiredArgsConstructor
public class ReserveControllerAdmin {

    private final ReservationService reservationService;
    private final LibraryService libraryService;

    @GetMapping
    public String getReservations(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long libraryId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<ReservationResponseDTO> page = reservationService
                .getFilteredReservations(clientId, itemId, libraryId, pageable);

        model.addAttribute("page", page);
        model.addAttribute("reservations", page.getContent());
        model.addAttribute("clientId", clientId);
        model.addAttribute("itemId", itemId);
        model.addAttribute("libraryId", libraryId);
        model.addAttribute("libraries", libraryService.getAllLibraries());

        return "admin/reservations/reservation-content";
    }

}
