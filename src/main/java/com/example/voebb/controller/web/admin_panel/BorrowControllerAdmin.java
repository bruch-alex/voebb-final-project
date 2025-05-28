package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.borrow.CreateBorrowDTO;
import com.example.voebb.model.dto.borrow.GetBorrowingsDTO;
import com.example.voebb.service.BorrowService;
import com.example.voebb.service.LibraryService;
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
@RequestMapping("admin/borrowings")
@RequiredArgsConstructor
public class BorrowControllerAdmin {

    private final BorrowService borrowService;
    private final LibraryService libraryService;

    @GetMapping
    public String getAllBorrowings(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long libraryId,
            @PageableDefault(size = 5,sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<GetBorrowingsDTO> page = borrowService.getFilteredBorrowings(userId, itemId, libraryId, pageable);

        model.addAttribute("page", page);
        model.addAttribute("borrowings", page.getContent());
        model.addAttribute("userId", userId);
        model.addAttribute("itemIdFilter", itemId);
        model.addAttribute("libraryId", libraryId);
        model.addAttribute("libraries", libraryService.getAllLibraries());

        return "admin/borrow/borrow-content";
    }

    @PostMapping
    public String borrowItem(@ModelAttribute CreateBorrowDTO dto,
                             RedirectAttributes redirectAttributes) {
        try {
            borrowService.createBorrow(dto);
            redirectAttributes.addFlashAttribute("success", "New borrow created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/borrowings";
    }

    @PostMapping("/return/{id}")
    public String returnBorrowedItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String message = borrowService.returnBorrow(id);
            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/borrowings";
    }

}
