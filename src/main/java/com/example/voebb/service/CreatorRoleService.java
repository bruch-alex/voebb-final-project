package com.example.voebb.service;

import com.example.voebb.model.dto.creator.CreatorRoleRequestDTO;
import com.example.voebb.model.dto.creator.CreatorRoleResponseDTO;

import java.util.List;

public interface CreatorRoleService {

    List<CreatorRoleResponseDTO> getAllCreatorRoles();

    CreatorRoleResponseDTO getCreatorRoleById(Long id);

    CreatorRoleResponseDTO createCreatorRole(CreatorRoleRequestDTO dto);

    CreatorRoleResponseDTO updateCreatorRole(Long id, CreatorRoleRequestDTO dto);

    void deleteCreatorRole(Long id);
}

