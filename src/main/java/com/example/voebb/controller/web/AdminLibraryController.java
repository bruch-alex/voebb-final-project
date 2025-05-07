package com.example.voebb.controller.web;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/libraries")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLibraryController {

    private final LibraryService libraryService;

    @Autowired
    public AdminLibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public String getAllLibraries(Model model) {
        List<LibraryDTO> libraries = libraryService.getAllLibraries();
        model.addAttribute("libraries", libraries);
        return "admin/libraries/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("library", new LibraryDTO());
        return "admin/libraries/form";
    }

    @PostMapping("/new")
    public String createLibrary(@Valid @ModelAttribute("library") LibraryDTO libraryDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/libraries/form";
        }

        if (libraryService.libraryExistsByName(libraryDTO.getName())) {
            result.rejectValue("name", "duplicate", "A library with this name already exists");
            return "admin/libraries/form";
        }

        LibraryDTO created = libraryService.createLibrary(libraryDTO);
        redirectAttributes.addFlashAttribute("success", "Library successfully created");
        return "redirect:/admin/libraries";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return libraryService.getLibraryById(id)
                .map(library -> {
                    model.addAttribute("library", library);
                    return "admin/libraries/form";
                })
                .orElse("redirect:/admin/libraries?error=Library+not+found");
    }

    @PostMapping("/{id}/edit")
    public String updateLibrary(@PathVariable Long id,
                                @Valid @ModelAttribute("library") LibraryDTO libraryDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/libraries/form";
        }

        // Check if name already exists for a different library
        if (libraryService.libraryExistsByName(libraryDTO.getName()) &&
                !libraryService.getLibraryById(id).get().getName().equals(libraryDTO.getName())) {
            result.rejectValue("name", "duplicate", "A library with this name already exists");
            return "admin/libraries/form";
        }

        return libraryService.updateLibrary(id, libraryDTO)
                .map(updated -> {
                    redirectAttributes.addFlashAttribute("success", "Library successfully updated");
                    return "redirect:/admin/libraries";
                })
                .orElse("redirect:/admin/libraries?error=Library+not+found");
    }

    @PostMapping("/{id}/delete")
    public String deleteLibrary(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = libraryService.deleteLibrary(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Library successfully deleted");
        } else {
            redirectAttributes.addFlashAttribute("error", "Library not found or could not be deleted");
        }
        return "redirect:/admin/libraries";
    }

    @GetMapping("/search")
    public String searchLibraries(@RequestParam String searchText, Model model) {
        List<LibraryDTO> libraries = libraryService.searchLibraries(searchText);
        model.addAttribute("libraries", libraries);
        model.addAttribute("searchText", searchText);
        return "admin/libraries/list";
    }
}