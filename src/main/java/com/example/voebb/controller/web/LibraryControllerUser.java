package com.example.voebb.controller.web;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Library;
import com.example.voebb.service.LibraryService;
import com.example.voebb.service.mapper.LibraryMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/libraries")
public class LibraryControllerUser {

    private final LibraryService libraryService;
    private final LibraryMapper libraryMapper;

    public LibraryControllerUser(LibraryService libraryService, LibraryMapper libraryMapper) {
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
        return "libraries/list";
    }

    @GetMapping("/{id}")
    public String getLibraryDetails(@PathVariable Long id, Model model) {
        Library library = libraryService.getLibraryById(id);
        model.addAttribute("library", libraryMapper.convertToDTO(library));
        return "libraries/details";
    }
}