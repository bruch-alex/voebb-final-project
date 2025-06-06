package com.example.voebb.controller.web.admin_panel;

import com.example.voebb.model.dto.library.*;
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
    public String getIndexPage(Model model,
                               @ModelAttribute("libraryFilters") LibraryFilters filters,
                               @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               @RequestParam(value = "success", required = false) String success) {

        Page<FullInfoLibraryDTO> page = libraryService.getFilteredLibrariesAdmin(filters, pageable);
        model.addAttribute("page", page);
        model.addAttribute("libraries", page.getContent());

        if (success != null && !success.isBlank()) {
            model.addAttribute("success", success);
        }

        return "admin/library/index";
    }

    @GetMapping("/new")
    public String showCreateLibraryForm(Model model) {
        model.addAttribute("createLibraryDTO", new CreateLibraryDTO());
        return "admin/library/create";
    }

    @PostMapping("/new")
    public String createLibrary(@ModelAttribute(name = "createLibraryDTO") CreateLibraryDTO createLibraryDTO) {
        libraryService.createLibrary(createLibraryDTO);
        return "redirect:/admin/libraries";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model) {
        EditLibraryDTO editLibraryDTO = libraryService.getLibraryById(id);
        model.addAttribute("library", editLibraryDTO);
        return "admin/library/edit";
    }

    @PostMapping("/edit/{id}")
    public String editLibrary(@PathVariable("id") Long id,
                              @ModelAttribute("library") EditLibraryDTO editLibraryDTO) {

        libraryService.updateLibrary(id, editLibraryDTO);
        return "redirect:/admin/libraries";

    }
}