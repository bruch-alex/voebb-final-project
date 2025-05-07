package com.example.voebb.controller.rest;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/libraries")
public class LibraryRestController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries() {
        List<LibraryDTO> libraries = libraryService.getAllLibraries();
        return ResponseEntity.ok(libraries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<LibraryDTO>> searchLibraries(@RequestParam String searchText) {
        List<LibraryDTO> libraries = libraryService.searchLibraries(searchText);
        return ResponseEntity.ok(libraries);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<LibraryDTO>> getLibrariesByLocation(@PathVariable String location) {
        List<LibraryDTO> libraries = libraryService.getLibrariesByLocation(location);
        return ResponseEntity.ok(libraries);
    }

    @PostMapping
    public ResponseEntity<?> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) {
        if (libraryService.libraryExistsByName(libraryDTO.getName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A library with this name already exists");
        }

        LibraryDTO createdLibrary = libraryService.createLibrary(libraryDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdLibrary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLibrary(@PathVariable Long id,
                                           @Valid @RequestBody LibraryDTO libraryDTO) {
        // Check if name already exists for a different library
        if (libraryService.libraryExistsByName(libraryDTO.getName()) &&
                !libraryService.getLibraryById(id).map(lib -> lib.getName().equals(libraryDTO.getName())).orElse(false)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A library with this name already exists");
        }

        return libraryService.updateLibrary(id, libraryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        boolean deleted = libraryService.deleteLibrary(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}