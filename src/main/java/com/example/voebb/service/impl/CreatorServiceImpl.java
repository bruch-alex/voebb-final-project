package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.CreatorFullNameDTO;
import com.example.voebb.model.dto.creator.CreatorResponseDTO;
import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.dto.creator.UpdateCreatorWithRoleDTO;
import com.example.voebb.model.entity.*;
import com.example.voebb.repository.CreatorProductRelationRepo;
import com.example.voebb.repository.CreatorRepo;
import com.example.voebb.repository.CreatorRoleRepo;
import com.example.voebb.service.CreatorRoleService;
import com.example.voebb.service.CreatorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepo creatorRepo;
    private final CreatorRoleService creatorRoleService;
    private final CreatorProductRelationRepo creatorProductRelationRepo;
    private final CreatorRoleRepo creatorRoleRepo;

    @Override
    @Transactional
    public void assignCreatorsToProduct(List<CreatorWithRoleDTO> creatorDTOs,
                                        Product product) {
        if (creatorDTOs == null || creatorDTOs.isEmpty()) return;

        for (CreatorWithRoleDTO dto : creatorDTOs) {

            String firstName = dto.getFirstName() == null ? "" : dto.getFirstName().trim();
            String lastName = dto.getLastName() == null ? "" : dto.getLastName().trim();
            String roleName = dto.getRole() == null ? "" : dto.getRole().trim();

            Creator creator = creatorRepo
                    .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
                    .orElseGet(() -> {
                        Creator c = new Creator();
                        c.setFirstName(firstName);
                        c.setLastName(lastName);
                        return creatorRepo.save(c);
                    });

            CreatorRole role = creatorRoleService.findOrCreate(roleName);

            CreatorProductRelation relation = new CreatorProductRelation();
            product.addRelation(relation);
            creator.addRelation(relation);
            relation.setCreatorRole(role);
        }
    }


    @Transactional
    public void updateCreatorsForProduct(Product product, List<UpdateCreatorWithRoleDTO> creatorsDto) {

        Set<CreatorRelationId> incomingRelationIds = creatorsDto.stream()
                .map(creator -> new CreatorRelationId(creator.getCreatorId(), product.getId(), creator.getCreatorRoleId()))
                .collect(Collectors.toSet());

        List<CreatorProductRelation> existingRelations = new ArrayList<>(product.getCreatorProductRelations());
        for (CreatorProductRelation existing : existingRelations) {
            if (!incomingRelationIds.contains(existing.getId())) {
                product.removeRelation(existing);
                creatorProductRelationRepo.delete(existing);
            }
        }

        for (UpdateCreatorWithRoleDTO dtoCreator : creatorsDto) {
            if (dtoCreator.getCreatorId() == null || dtoCreator.getCreatorRoleId() == null) {
                continue;
            }

            CreatorRelationId id = new CreatorRelationId(
                    dtoCreator.getCreatorId(), product.getId(), dtoCreator.getCreatorRoleId()
            );

            boolean alreadyExists = product.getCreatorProductRelations().stream()
                    .anyMatch(relation -> relation.getId().equals(id));

            if (!alreadyExists) {
                Creator creator = creatorRepo.findById(dtoCreator.getCreatorId())
                        .orElseThrow(() -> new RuntimeException("Creator not found: " + dtoCreator.getCreatorId()));
                CreatorRole role = creatorRoleRepo.findById(dtoCreator.getCreatorRoleId())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + dtoCreator.getCreatorRoleId()));

                CreatorProductRelation newRelation = new CreatorProductRelation();
                newRelation.setId(id);
                newRelation.setCreator(creator);
                newRelation.setProduct(product);
                newRelation.setCreatorRole(role);

                product.addRelation(newRelation);
                creator.addRelation(newRelation);
            }
        }
    }

    @Override
    public Set<CreatorResponseDTO> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }

        String[] nameParts = name.split(" ");

        Set<CreatorResponseDTO> creators = new HashSet<>();

        for (var namePart : nameParts) {
            creatorRepo.searchByNameNative(namePart)
                    .stream()
                    .map(creator -> new CreatorResponseDTO(
                            creator.getId(),
                            creator.getFirstName(),
                            creator.getLastName()
                    ))
                    .forEach(creators::add);
        }
        return creators;
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
    @Transactional
    public CreatorResponseDTO saveCreator(CreatorFullNameDTO dto) {
        Creator newCreator = new Creator();
        newCreator.setFirstName(dto.firstName());
        newCreator.setLastName(dto.lastName());
        Creator savedCreator = creatorRepo.save(newCreator);
        return new CreatorResponseDTO(savedCreator.getId(), savedCreator.getFirstName(), savedCreator.getLastName());
    }

    @Override
    public CreatorResponseDTO updateCreator(Long id, CreatorFullNameDTO dto) {
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

    @Override
    public List<CreatorWithRoleDTO> getCreatorsWithRolesByProductId(Long id) {
        return creatorProductRelationRepo.findByProductId(id).stream()
                .map(rel -> new CreatorWithRoleDTO(
                        rel.getCreator().getFirstName(),
                        rel.getCreator().getLastName(),
                        rel.getCreatorRole().getCreatorRoleName()
                ))
                .collect(Collectors.toList());
    }
}
