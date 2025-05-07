package com.example.voebb.service.impl;

import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.entity.*;
import com.example.voebb.repository.CreatorProductRelationRepo;
import com.example.voebb.repository.CreatorRepo;
import com.example.voebb.repository.CreatorRoleRepo;
import com.example.voebb.repository.ProductRepo;
import com.example.voebb.service.CreatorProductRelationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatorProductRelationServiceImpl implements CreatorProductRelationService {

    private final CreatorProductRelationRepo relationRepo;
    private final CreatorRepo creatorRepo;
    private final ProductRepo productRepo;
    private final CreatorRoleRepo creatorRoleRepo;

    public CreatorProductRelationServiceImpl(CreatorProductRelationRepo relationRepo,
                                             CreatorRepo creatorRepo,
                                             ProductRepo productRepo,
                                             CreatorRoleRepo creatorRoleRepo) {
        this.relationRepo = relationRepo;
        this.creatorRepo = creatorRepo;
        this.productRepo = productRepo;
        this.creatorRoleRepo = creatorRoleRepo;
    }


    @Override
    public void assignCreatorToProduct(Long creatorId, Long productId, Long roleId) {


        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Creator creator = creatorRepo.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));
        CreatorRole role = creatorRoleRepo.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        CreatorProductRelation relation = new CreatorProductRelation();
        relation.setProduct(product);
        relation.setCreator(creator);
        relation.setCreatorRole(role);
        relation.setId(new CreatorRelationId(creatorId, productId, roleId));

        relationRepo.save(relation);
    }

    @Override
    public List<CreatorWithRoleDTO> getCreatorsByProductId(Long productId) {
        return relationRepo.findByProductId(productId).stream()
                .map(relation -> new CreatorWithRoleDTO(
                        relation.getCreator().getId(),
                        relation.getCreator().getFirstName(),
                        relation.getCreator().getLastName(),
                        relation.getCreatorRole().getId(),
                        relation.getCreatorRole().getCreatorRole()
                ))
                .toList();
    }
}
