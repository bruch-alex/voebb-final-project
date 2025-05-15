package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.library.EditLibraryDTO;
import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/libraries")
@RequiredArgsConstructor
public class LibraryControllerAdmin {
    private final LibraryService libraryService;

    @GetMapping
    public String getIndexPage(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               Model model) {
        Page<LibraryDTO> page = libraryService.getAllLibraries(pageable);
        model.addAttribute("page", page);
        model.addAttribute("libraries", page.getContent());
        return "admin/library/index";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable("id") Long id,
                              Model model) {
        EditLibraryDTO editLibraryDTO = libraryService.getLibraryById(id);
        model.addAttribute("library", editLibraryDTO);
        return "admin/library/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("library") EditLibraryDTO editLibraryDTO) {

        libraryService.updateLibrary(id, editLibraryDTO);
        return "redirect:/admin/libraries";

    }

}
