package com.example.voebb.controller.web;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Library;
import com.example.voebb.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryControllerUser {

    private final LibraryService libraryService;

    public LibraryControllerUser(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public String getAllLibraries(Model model) {
        List<Library> libraries = libraryService.getAllLibraries();
        List<LibraryDTO> libraryDTOs = libraries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("libraries", libraryDTOs);
        return "libraries/list";
    }

    @GetMapping("/{id}")
    public String getLibraryDetails(@PathVariable Long id, Model model) {
        Library library = libraryService.getLibraryById(id);
        model.addAttribute("library", convertToDTO(library));
        return "libraries/details";
    }

    private LibraryDTO convertToDTO(Library library) {
        Address address = library.getAddress();
        return new LibraryDTO(
                library.getId(),
                library.getName(),
                library.getDescription(),
                address != null ? address.getCity() : null,
                address != null ? address.getDistrict() : null,
                address != null ? address.getPostcode() : null,
                address != null ? address.getStreet() : null,
                address != null ? address.getHouseNr() : null,
                address != null ? address.getOsmLink() : null
        );
    }
}