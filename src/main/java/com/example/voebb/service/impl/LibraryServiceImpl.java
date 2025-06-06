package com.example.voebb.service.impl;

import com.example.voebb.model.dto.library.*;
import com.example.voebb.model.entity.Library;
import com.example.voebb.model.mapper.AddressMapper;
import com.example.voebb.model.mapper.LibraryMapper;
import com.example.voebb.repository.LibraryRepo;
import com.example.voebb.service.LibraryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepo libraryRepo;

    @Transactional
    @Override
    public void createLibrary(CreateLibraryDTO createLibraryDTO) {
        if (libraryRepo.existsByName(createLibraryDTO.getName())) {
            throw new RuntimeException("Library with this name already exists");
        }
        Library newLibrary = LibraryMapper.toNewEntity(createLibraryDTO);
        libraryRepo.save(newLibrary);
    }

    @Override
    public EditLibraryDTO getLibraryById(Long libraryId) {
        return libraryRepo.getLibraryFullInfo(libraryId);
    }

    @Override
    public List<LibraryDTO> getAllLibraries() {
        return libraryRepo.getAllInDTO();
    }

    @Override
    public List<String> getAllDistricts() {
        return libraryRepo.findAllDistricts();
    }

    @Override
    public Page<FullInfoLibraryDTO> getFilteredLibrariesAdmin(LibraryFilters filters, Pageable pageable) {
        return libraryRepo
                .findFilteredLibrariesForAdmin(
                        filters.getLibraryId(),
                        filters.getName(),
                        filters.getDistrict(),
                        pageable)
                .map(LibraryMapper::toFullInfoDTO);
    }


    @Transactional
    @Override
    public EditLibraryDTO updateLibrary(Long libraryId, EditLibraryDTO editLibraryDTO) {
        Library oldLibrary = libraryRepo.findById(libraryId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        oldLibrary.setAddress(AddressMapper.toEntity(editLibraryDTO));
        oldLibrary.setName(editLibraryDTO.name());
        oldLibrary.setDescription(editLibraryDTO.description());
        Library savedLibrary = libraryRepo.save(oldLibrary);
        return LibraryMapper.toEditDTO(savedLibrary);
    }

    @Override
    public void deleteLibraryById(Long libraryId) {
        if (!libraryRepo.existsById(libraryId)) {
            throw new RuntimeException("Library not found");
        }
        libraryRepo.deleteById(libraryId);
    }
}
