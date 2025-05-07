package com.example.voebb.service;


import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;

import java.util.List;

public interface CreatorProductRelationService {

    void assignCreatorToProduct(Long creatorId, Long productId, Long roleId);

    List<CreatorWithRoleDTO> getCreatorsByProductId(Long productId);

}

