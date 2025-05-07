package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.CreatorRequestDTO;
import com.example.voebb.model.dto.creator.CreatorResponseDTO;
import com.example.voebb.model.entity.Creator;
import com.example.voebb.repository.CreatorRepo;
import com.example.voebb.service.CreatorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepo creatorRepo;

    public CreatorServiceImpl(CreatorRepo creatorRepo) {
        this.creatorRepo = creatorRepo;
    }


    @Override
    public List<CreatorResponseDTO> getAllCreators() {
        return creatorRepo.findAll().stream()
                .map(c -> new CreatorResponseDTO(c.getId(), c.getFirstName(), c.getLastName()))
                .toList();
    }

    @Override
    public CreatorResponseDTO getCreatorById(Long id) {
        return creatorRepo.findById(id)
                .map(c -> new CreatorResponseDTO(c.getId(), c.getFirstName(), c.getLastName()))
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));
    }

    @Override
    public CreatorResponseDTO saveCreator(CreatorRequestDTO dto) {
        Creator newCreator = new Creator(null, dto.firstName(), dto.lastName());
        Creator savedCreator = creatorRepo.save(newCreator);
        return new CreatorResponseDTO(savedCreator.getId(), savedCreator.getFirstName(), savedCreator.getLastName());
    }

    @Override
    public CreatorResponseDTO updateCreator(Long id, CreatorRequestDTO dto) {
        Creator existingCreator = creatorRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));

        existingCreator.setFirstName(dto.firstName());
        existingCreator.setLastName(dto.lastName());

        Creator updated = creatorRepo.save(existingCreator);
        return new CreatorResponseDTO(updated.getId(), updated.getFirstName(), updated.getLastName());
    }

    @Override
    public void deleteCreator(Long id) {
        if (!creatorRepo.existsById(id)) {
            throw new EntityNotFoundException("Creator not found");
        }
        creatorRepo.deleteById(id);
    }
}
