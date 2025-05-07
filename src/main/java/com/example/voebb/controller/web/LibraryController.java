package com.example.voebb.controller.web;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public String getAllLibraries(Model model) {
        List<LibraryDTO> libraries = libraryService.getAllLibraries();
        model.addAttribute("libraries", libraries);
        return "libraries/list";
    }

    @GetMapping("/search")
    public String searchLibraries(@RequestParam String searchText, Model model) {
        List<LibraryDTO> libraries = libraryService.searchLibraries(searchText);
        model.addAttribute("libraries", libraries);
        model.addAttribute("searchText", searchText);
        return "libraries/list";
    }

    @GetMapping("/{id}")
    public String getLibraryDetails(@PathVariable Long id, Model model) {
        return libraryService.getLibraryById(id)
                .map(library -> {
                    model.addAttribute("library", library);
                    return "libraries/details";
                })
                .orElse("redirect:/libraries?error=Library+not+found");
    }

    @GetMapping("/new")
    public String showLibraryForm(Model model) {
        model.addAttribute("library", new LibraryDTO());
        model.addAttribute("isNew", true);
        return "libraries/form";
    }

    @PostMapping("/new")
    public String createLibrary(@Valid @ModelAttribute("library") LibraryDTO libraryDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "libraries/form";
        }

        if (libraryService.libraryExistsByName(libraryDTO.getName())) {
            result.rejectValue("name", "error.library", "A library with this name already exists");
            return "libraries/form";
        }

        LibraryDTO createdLibrary = libraryService.createLibrary(libraryDTO);
        redirectAttributes.addFlashAttribute("success", "Library successfully created");
        return "redirect:/libraries/" + createdLibrary.getId();
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return libraryService.getLibraryById(id)
                .map(library -> {
                    model.addAttribute("library", library);
                    model.addAttribute("isNew", false);
                    return "libraries/form";
                })
                .orElse("redirect:/libraries?error=Library+not+found");
    }

    @PostMapping("/{id}/edit")
    public String updateLibrary(@PathVariable Long id,
                                @Valid @ModelAttribute("library") LibraryDTO libraryDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "libraries/form";
        }

        return libraryService.updateLibrary(id, libraryDTO)
                .map(updated -> {
                    redirectAttributes.addFlashAttribute("success", "Library successfully updated");
                    return "redirect:/libraries/" + updated.getId();
                })
                .orElse("redirect:/libraries?error=Library+not+found");
    }

    @PostMapping("/{id}/delete")
    public String deleteLibrary(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = libraryService.deleteLibrary(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("success", "Library successfully deleted");
        } else {
            redirectAttributes.addFlashAttribute("error", "Library not found");
        }
        return "redirect:/libraries";
    }

    @GetMapping("/location/{location}")
    public String getLibrariesByLocation(@PathVariable String location, Model model) {
        List<LibraryDTO> libraries = libraryService.getLibrariesByLocation(location);
        model.addAttribute("libraries", libraries);
        model.addAttribute("location", location);
        return "libraries/location";
    }
}