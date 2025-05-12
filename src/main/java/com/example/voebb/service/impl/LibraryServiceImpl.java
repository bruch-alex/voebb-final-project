package com.example.voebb.service.impl;

import com.example.voebb.model.entity.Library;
import com.example.voebb.repository.LibraryRepo;
import com.example.voebb.service.LibraryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepo libraryRepo;

    public LibraryServiceImpl(LibraryRepo libraryRepo) {
        this.libraryRepo = libraryRepo;
    }

    @Override
    public void createLibrary(Library library) {
        this.libraryRepo.save(library);
    }

    @Override
    public List<Library> getAllLibraries() {
        return libraryRepo.findAll();
    }

    @Override
    public Library getLibraryById(Long libraryId) {
        return libraryRepo.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Library not found"));
    }

    @Override
    public Library updateLibrary(Long libraryId, Library newLibrary) {
        Library oldLibrary = getLibraryById(libraryId);

        oldLibrary.setAddress(newLibrary.getAddress());
        oldLibrary.setName(newLibrary.getName());
        oldLibrary.setDescription(newLibrary.getDescription());
        return libraryRepo.save(oldLibrary);
    }

    @Override
    public void deleteLibraryById(Long libraryId) {
        if (!libraryRepo.existsById(libraryId)) {
            throw new RuntimeException("Library not found");
        }
        libraryRepo.deleteById(libraryId);
    }
}