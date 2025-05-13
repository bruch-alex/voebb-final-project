package com.example.voebb.controller.web;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Library;
import com.example.voebb.service.LibraryService;
import com.example.voebb.service.mapper.LibraryMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/libraries")
public class LibraryControllerAdmin {

    private final LibraryService libraryService;
    private final LibraryMapper libraryMapper;

    public LibraryControllerAdmin(LibraryService libraryService, LibraryMapper libraryMapper) {
        this.libraryService = libraryService;
        this.libraryMapper = libraryMapper;
    }

    @GetMapping
    public String getAllLibraries(Model model) {
        List<Library> libraries = libraryService.getAllLibraries();
        List<LibraryDTO> libraryDTOs = libraries.stream()
                .map(libraryMapper::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("libraries", libraryDTOs);
        return "admin/libraries/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("library", new LibraryDTO());
        return "admin/libraries/create";
    }

    @PostMapping
    public String createLibrary(@ModelAttribute LibraryDTO libraryDTO, RedirectAttributes ra) {
        Library library = libraryMapper.convertToEntity(libraryDTO);
        libraryService.createLibrary(library);
        ra.addFlashAttribute("success", "Library created successfully");
        return "redirect:/admin/libraries";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Library library = libraryService.getLibraryById(id);
        model.addAttribute("library", libraryMapper.convertToDTO(library));
        return "admin/libraries/edit";
    }

    @PostMapping("/update/{id}")
    public String updateLibrary(@PathVariable Long id, @ModelAttribute LibraryDTO libraryDTO, RedirectAttributes ra) {
        Library library = libraryMapper.convertToEntity(libraryDTO);
        libraryService.updateLibrary(id, library);
        ra.addFlashAttribute("success", "Library updated successfully");
        return "redirect:/admin/libraries";
    }

    @PostMapping("/delete/{id}")
    public String deleteLibrary(@PathVariable Long id, RedirectAttributes ra) {
        libraryService.deleteLibraryById(id);
        ra.addFlashAttribute("success", "Library deleted successfully");
        return "redirect:/admin/libraries";
    }
}