package com.example.voebb.service;

import com.example.voebb.model.dto.creator.CreatorRequestDTO;
import com.example.voebb.model.dto.creator.CreatorResponseDTO;

import java.util.List;

public interface CreatorService {

    List<CreatorResponseDTO> getAllCreators();

    CreatorResponseDTO getCreatorById(Long id);

    CreatorResponseDTO saveCreator(CreatorRequestDTO dto);

    CreatorResponseDTO updateCreator(Long id, CreatorRequestDTO dto);

    void deleteCreator(Long id);
}
