package com.example.voebb.service;

import com.example.voebb.model.dto.library.*;
import com.example.voebb.model.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibraryService {

    void createLibrary(CreateLibraryDTO createLibraryDTO);

    EditLibraryDTO getLibraryById(Long libraryId);

    List<LibraryDTO> getAllLibraries();

    List<String> getAllDistricts();

    Page<FullInfoLibraryDTO> getFilteredLibrariesAdmin(LibraryFilters filters, Pageable pageable);

    EditLibraryDTO updateLibrary(Long libraryId, EditLibraryDTO editLibraryDTO);

    void deleteLibraryById(Long id);
}
