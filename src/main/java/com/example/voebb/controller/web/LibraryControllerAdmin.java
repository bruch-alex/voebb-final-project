package com.example.voebb.controller.web;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Address;
import com.example.voebb.model.entity.Library;
import com.example.voebb.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/libraries")
@RequiredArgsConstructor
public class LibraryControllerAdmin {

    private final LibraryService libraryService;

    public LibraryControllerAdmin(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public String getAllLibraries(Model model) {
        List<Library> libraries = libraryService.getAllLibraries();
        List<LibraryDTO> libraryDTOs = libraries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("libraries", libraryDTOs);
        model.addAttribute("newLibrary", new LibraryDTO());
        return "admin/libraries/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("library", new LibraryDTO());
        return "admin/libraries/create";
    }

    @PostMapping
    public String createLibrary(@ModelAttribute LibraryDTO libraryDTO, RedirectAttributes ra) {
        Library library = convertToEntity(libraryDTO);
        libraryService.createLibrary(library);
        ra.addFlashAttribute("success", "Library created successfully");
        return "redirect:/admin/libraries";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Library library = libraryService.getLibraryById(id);
        model.addAttribute("library", convertToDTO(library));
        return "admin/libraries/edit";
    }

    @PostMapping("/update/{id}")
    public String updateLibrary(@PathVariable Long id, @ModelAttribute LibraryDTO libraryDTO, RedirectAttributes ra) {
        Library library = convertToEntity(libraryDTO);
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

    private LibraryDTO convertToDTO(Library library) {
        Address address = library.getAddress();
        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setDescription(library.getDescription());

        if (address != null) {
            dto.setCity(address.getCity());
            dto.setDistrict(address.getDistrict());
            dto.setPostcode(address.getPostcode());
            dto.setStreet(address.getStreet());
            dto.setHouseNr(address.getHouseNr());
            dto.setOsmLink(address.getOsmLink());
        }

        return dto;
    }

    private Library convertToEntity(LibraryDTO dto) {
        Library library = new Library();
        library.setId(dto.getId());
        library.setName(dto.getName());
        library.setDescription(dto.getDescription());

        Address address = new Address();
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setPostcode(dto.getPostcode());
        address.setStreet(dto.getStreet());
        address.setHouseNr(dto.getHouseNr());
        address.setOsmLink(dto.getOsmLink());
        library.setAddress(address);

        return library;
    }
}