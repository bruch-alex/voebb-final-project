package com.example.voebb.service;

import com.example.voebb.model.dto.library.CreateLibraryDTO;
import com.example.voebb.model.dto.library.EditLibraryDTO;
import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.model.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibraryService {
    void createLibrary(CreateLibraryDTO createLibraryDTO);

    List<LibraryDTO> getAllLibraries();

    Page<LibraryDTO> getAllLibraries(Pageable pageable);

    EditLibraryDTO getLibraryById(Long libraryId);

    EditLibraryDTO updateLibrary(Long libraryId, EditLibraryDTO editLibraryDTO);

    void deleteLibraryById(Long id);
}
