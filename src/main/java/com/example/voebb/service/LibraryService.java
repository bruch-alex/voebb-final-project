package com.example.voebb.service;

import com.example.voebb.model.dto.LibraryDTO;
import com.example.voebb.model.entity.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryService {

    /**
     * Get all libraries
     * @return list of all libraries
     */
    List<LibraryDTO> getAllLibraries();

    /**
     * Get a library by its ID
     * @param id the library ID
     * @return the library if found
     */
    Optional<LibraryDTO> getLibraryById(Long id);

    /**
     * Get libraries by location
     * @param location the location to search for
     * @return list of libraries in the specified location
     */
    List<LibraryDTO> getLibrariesByLocation(String location);

    /**
     * Create a new library
     * @param libraryDTO the library data
     * @return the created library
     */
    LibraryDTO createLibrary(LibraryDTO libraryDTO);

    /**
     * Update an existing library
     * @param id the library ID
     * @param libraryDTO the updated library data
     * @return the updated library
     */
    Optional<LibraryDTO> updateLibrary(Long id, LibraryDTO libraryDTO);

    /**
     * Delete a library
     * @param id the library ID
     * @return true if the library was deleted
     */
    boolean deleteLibrary(Long id);

    /**
     * Search for libraries
     * @param searchText the text to search for in name or description
     * @return list of matching libraries
     */
    List<LibraryDTO> searchLibraries(String searchText);

    /**
     * Check if a library with the given name exists
     * @param name the library name
     * @return true if a library with the name exists
     */
    boolean libraryExistsByName(String name);
}