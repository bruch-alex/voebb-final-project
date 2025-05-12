package com.example.voebb.service;

import com.example.voebb.model.entity.Library;

import java.util.List;

public interface LibraryService {
    void createLibrary(Library library);

    List<Library> getAllLibraries();

    Library getLibraryById(Long libraryId);

    Library updateLibrary(Long libraryId, Library newLibrary);

    void deleteLibraryById(Long id);
}