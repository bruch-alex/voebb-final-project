package com.example.voebb.service;

import com.example.voebb.model.dto.creator.CreatorFullNameDTO;
import com.example.voebb.model.dto.creator.CreatorResponseDTO;
import com.example.voebb.model.dto.creator.CreatorWithRoleDTO;
import com.example.voebb.model.entity.Product;

import java.util.List;

public interface CreatorService {

    void assignCreatorsToProduct(List<CreatorWithRoleDTO> creatorDTOs, Product product);

    List<CreatorResponseDTO> searchByLastName(String lastName);

    List<CreatorResponseDTO> getAllCreators();

    CreatorResponseDTO getCreatorById(Long id);

    CreatorResponseDTO saveCreator(CreatorFullNameDTO dto);

    CreatorResponseDTO updateCreator(Long id, CreatorFullNameDTO dto);

    void deleteCreator(Long id);
}
