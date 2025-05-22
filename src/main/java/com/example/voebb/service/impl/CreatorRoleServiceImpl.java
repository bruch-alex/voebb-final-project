package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.AddCreatorRoleDTO;
import com.example.voebb.model.dto.creator.CreatorRoleResponseDTO;
import com.example.voebb.model.entity.CreatorRole;
import com.example.voebb.repository.CreatorRoleRepo;
import com.example.voebb.service.CreatorRoleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorRoleServiceImpl implements CreatorRoleService {

    public final CreatorRoleRepo creatorRoleRepo;

    @Override
    @Transactional
    public CreatorRole findOrCreate(String roleName) {


        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Creator role must be non-empty");
        }

        String sanitizedName = roleName.trim();

        return creatorRoleRepo.findByCreatorRoleNameIgnoreCase(sanitizedName)
                .orElseGet(() -> {
                    CreatorRole role = new CreatorRole();
                    role.setCreatorRoleName(sanitizedName);
                    return creatorRoleRepo.save(role);
                });
    }

    @Override
    public List<CreatorRoleResponseDTO> getAllCreatorRoles() {
        return creatorRoleRepo.findAll().stream()
                .map(role -> new CreatorRoleResponseDTO(role.getId(), role.getCreatorRoleName()))
                .toList();
    }

    @Override
    public List<CreatorRoleResponseDTO> searchByRoleName(String roleName) {

        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Creator role name cannot be null or empty");
        }

        return creatorRoleRepo.findTop5ByCreatorRoleNameContainingIgnoreCase(roleName)
                .stream()
                .map(role -> new CreatorRoleResponseDTO(
                        role.getId(),
                        role.getCreatorRoleName()))
                .toList();
    }

    @Override
    public CreatorRoleResponseDTO getCreatorRoleById(Long id) {
        return creatorRoleRepo.findById(id)
                .map(role -> new CreatorRoleResponseDTO(role.getId(), role.getCreatorRoleName()))
                .orElseThrow(() -> new EntityNotFoundException("Creator role not found"));
    }

    @Override
    public CreatorRoleResponseDTO saveCreatorRole(AddCreatorRoleDTO dto) {
        CreatorRole newRole = new CreatorRole();
        newRole.setCreatorRoleName(dto.creatorRoleName());
        creatorRoleRepo.save(newRole);
        return new CreatorRoleResponseDTO(newRole.getId(), newRole.getCreatorRoleName());
    }

    @Override
    public void updateCreatorRole(Long id, String roleName) {
        CreatorRole existing = creatorRoleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Creator role not found"));

        existing.setCreatorRoleName(roleName);
        CreatorRole updated = creatorRoleRepo.save(existing);
    }

    @Override
    public void deleteCreatorRole(Long id) {
        if (!creatorRoleRepo.existsById(id)) {
            throw new EntityNotFoundException("Creator role not found");
        }
        creatorRoleRepo.deleteById(id);
    }
}
