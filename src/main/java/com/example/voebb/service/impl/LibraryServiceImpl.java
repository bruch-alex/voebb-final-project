package com.example.voebb.service.impl;

import com.example.voebb.model.dto.library.EditLibraryDTO;
import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.model.entity.Address;
import com.example.voebb.model.entity.Library;
import com.example.voebb.repository.LibraryRepo;
import com.example.voebb.service.LibraryService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepo libraryRepo;

    public LibraryServiceImpl(LibraryRepo libraryRepo) {
        this.libraryRepo = libraryRepo;
    }

    @Override
    @Transactional
    public void createLibrary(Library library) {
        this.libraryRepo.save(library);
    }

    @Override
    public Page<LibraryDTO> getAllLibraries(Pageable pageable) {
        return libraryRepo.getLibrariesForAdmin(pageable);
    }

    @Override
    public List<Library> getAllLibraries() {
        return libraryRepo.findAll();
    }

    @Override
    public EditLibraryDTO getLibraryById(Long libraryId) {
        return libraryRepo.getLibraryFullInfo(libraryId);
    }

    @Override
    @Transactional
    public EditLibraryDTO updateLibrary(Long libraryId, EditLibraryDTO editLibraryDTO) {
        Library oldLibrary = libraryRepo.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        oldLibrary.setAddress(toAddress(editLibraryDTO));
        oldLibrary.setName(editLibraryDTO.name());
        oldLibrary.setDescription(editLibraryDTO.description());
        Library savedLibrary = libraryRepo.save(oldLibrary);
        return toEditDTO(savedLibrary);
    }

    @Override
    @Transactional
    public void deleteLibraryById(Long libraryId) {
        if (!libraryRepo.existsById(libraryId)) {
            throw new RuntimeException("Library not found");
        }
        libraryRepo.deleteById(libraryId);
    }

    private EditLibraryDTO toEditDTO(Library library) {
        return new EditLibraryDTO(
                library.getId(),
                library.getName(),
                library.getDescription(),
                library.getAddress().getPostcode(),
                library.getAddress().getCity(),
                library.getAddress().getDistrict(),
                library.getAddress().getStreet(),
                library.getAddress().getHouseNr(),
                library.getAddress().getOsmLink()
        );
    }

    private Address toAddress(EditLibraryDTO editLibraryDTO) {
        return new Address(
                editLibraryDTO.city(),
                editLibraryDTO.district(),
                editLibraryDTO.postcode(),
                editLibraryDTO.street(),
                editLibraryDTO.houseNumber(),
                editLibraryDTO.osmLink()
        );
    }
}
