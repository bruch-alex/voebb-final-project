package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.CreatorRoleRequestDTO;
import com.example.voebb.model.dto.creator.CreatorRoleResponseDTO;
import com.example.voebb.model.entity.CreatorRole;
import com.example.voebb.repository.CreatorRoleRepo;
import com.example.voebb.service.CreatorRoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatorRoleServiceImpl implements CreatorRoleService {

    public final CreatorRoleRepo creatorRoleRepo;

    public CreatorRoleServiceImpl(CreatorRoleRepo creatorRoleRepo) {
        this.creatorRoleRepo = creatorRoleRepo;
    }

    @Override
    @Transactional
    public CreatorRole findOrCreate(String roleName) {


        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Creator role must be non-empty");
        }

        String sanitizedName = roleName.trim();

        return creatorRoleRepo.findByCreatorRoleIgnoreCase(sanitizedName)
                .orElseGet(() -> {
                    CreatorRole role = new CreatorRole();
                    role.setCreatorRole(sanitizedName);
                    return creatorRoleRepo.save(role);
                });
    }

    @Override
    public List<CreatorRoleResponseDTO> getAllCreatorRoles() {
        return creatorRoleRepo.findAll().stream()
                .map(role -> new CreatorRoleResponseDTO(role.getId(), role.getCreatorRole()))
                .toList();
    }

    @Override
    public CreatorRoleResponseDTO getCreatorRoleById(Long id) {
        return creatorRoleRepo.findById(id)
                .map(role -> new CreatorRoleResponseDTO(role.getId(), role.getCreatorRole()))
                .orElseThrow(() -> new EntityNotFoundException("Creator role not found"));
    }

    @Override
    public CreatorRoleResponseDTO createCreatorRole(CreatorRoleRequestDTO dto) {
        CreatorRole role = new CreatorRole(null, dto.creatorRole());
        CreatorRole saved = creatorRoleRepo.save(role);
        return new CreatorRoleResponseDTO(saved.getId(), saved.getCreatorRole());
    }

    @Override
    public CreatorRoleResponseDTO updateCreatorRole(Long id, CreatorRoleRequestDTO dto) {
        CreatorRole existing = creatorRoleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Creator role not found"));

        existing.setCreatorRole(dto.creatorRole());
        CreatorRole updated = creatorRoleRepo.save(existing);
        return new CreatorRoleResponseDTO(updated.getId(), updated.getCreatorRole());
    }

    @Override
    public void deleteCreatorRole(Long id) {
        if (!creatorRoleRepo.existsById(id)) {
            throw new EntityNotFoundException("Creator role not found");
        }
        creatorRoleRepo.deleteById(id);
    }
}
