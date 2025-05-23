package com.example.voebb.service;

import com.example.voebb.model.dto.creator.CreatorFullNameDTO;
import com.example.voebb.model.dto.creator.CreatorResponseDTO;
import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.entity.Product;

import java.util.List;
import java.util.Set;

public interface CreatorService {

    void assignCreatorsToProduct(List<CreatorWithRoleDTO> creatorDTOs, Product product);

    Set<CreatorResponseDTO> searchByName(String lastName);

    List<CreatorResponseDTO> getAllCreators();

    CreatorResponseDTO getCreatorById(Long id);

    CreatorResponseDTO saveCreator(CreatorFullNameDTO dto);

    CreatorResponseDTO updateCreator(Long id, CreatorFullNameDTO dto);

    void deleteCreator(Long id);
}
