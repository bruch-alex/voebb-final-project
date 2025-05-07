package com.example.voebb.service.impl;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Library;
import com.example.voebb.repository.LibraryRepository;
import com.example.voebb.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryServiceImpl(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @Override
    public List<LibraryDTO> getAllLibraries() {
        return libraryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LibraryDTO> getLibraryById(Long id) {
        return libraryRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<LibraryDTO> getLibrariesByLocation(String location) {
        return libraryRepository.findByLocation(location).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LibraryDTO createLibrary(LibraryDTO libraryDTO) {
        Library library = convertToEntity(libraryDTO);
        Library savedLibrary = libraryRepository.save(library);
        return convertToDTO(savedLibrary);
    }

    @Override
    @Transactional
    public Optional<LibraryDTO> updateLibrary(Long id, LibraryDTO libraryDTO) {
        return libraryRepository.findById(id)
                .map(existingLibrary -> {
                    // Update the existing library with new values
                    existingLibrary.setName(libraryDTO.getName());
                    existingLibrary.setLocation(libraryDTO.getLocation());
                    existingLibrary.setAddress(libraryDTO.getAddress());
                    existingLibrary.setDescription(libraryDTO.getDescription());
                    existingLibrary.setOpeningHours(libraryDTO.getOpeningHours());
                    existingLibrary.setContactInfo(libraryDTO.getContactInfo());
                    existingLibrary.setEmail(libraryDTO.getEmail());
                    existingLibrary.setPhone(libraryDTO.getPhone());
                    existingLibrary.setWebsite(libraryDTO.getWebsite());

                    // Save the updated library
                    return convertToDTO(libraryRepository.save(existingLibrary));
                });
    }

    @Override
    @Transactional
    public boolean deleteLibrary(Long id) {
        if (libraryRepository.existsById(id)) {
            libraryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<LibraryDTO> searchLibraries(String searchText) {
        return libraryRepository.searchLibraries(searchText).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean libraryExistsByName(String name) {
        return libraryRepository.existsByName(name);
    }

    // Helper methods for conversion between Entity and DTO
    private LibraryDTO convertToDTO(Library library) {
        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setLocation(library.getLocation());
        dto.setAddress(library.getAddress());
        dto.setDescription(library.getDescription());
        dto.setOpeningHours(library.getOpeningHours());
        dto.setContactInfo(library.getContactInfo());
        dto.setEmail(library.getEmail());
        dto.setPhone(library.getPhone());
        dto.setWebsite(library.getWebsite());
        return dto;
    }

    private Library convertToEntity(LibraryDTO dto) {
        Library library = new Library();
        library.setId(dto.getId());
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());
        library.setAddress(dto.getAddress());
        library.setDescription(dto.getDescription());
        library.setOpeningHours(dto.getOpeningHours());
        library.setContactInfo(dto.getContactInfo());
        library.setEmail(dto.getEmail());
        library.setPhone(dto.getPhone());
        library.setWebsite(dto.getWebsite());
        return library;
    }
}