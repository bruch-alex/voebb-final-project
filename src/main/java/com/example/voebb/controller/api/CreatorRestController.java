package com.example.voebb.controller.api;

import com.example.voebb.model.dto.creator.AddCreatorRoleDTO;
import com.example.voebb.model.dto.creator.CreatorFullNameDTO;
import com.example.voebb.model.dto.creator.CreatorResponseDTO;
import com.example.voebb.model.dto.creator.CreatorRoleResponseDTO;
import com.example.voebb.service.CreatorRoleService;
import com.example.voebb.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/creators")
@RequiredArgsConstructor
public class CreatorRestController {

    private final CreatorService creatorService;
    private final CreatorRoleService creatorRoleService;

    @GetMapping("/searchCreator")
    public Set<CreatorResponseDTO> searchByLastName(@RequestParam String lastName) {
        return creatorService.searchByName(lastName.trim());
    }

    @PostMapping("/newCreator")
    public ResponseEntity<CreatorResponseDTO> addNewCreator(@RequestBody CreatorFullNameDTO dto) {
        CreatorResponseDTO savedCreator = creatorService.saveCreator(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCreator);
    }


    @GetMapping("/searchRole")
    public List<CreatorRoleResponseDTO> searchByRoleName(@RequestParam String roleName) {
        return creatorRoleService.searchByRoleName(roleName.trim());
    }


    @PostMapping("/newRole")
    public ResponseEntity<CreatorRoleResponseDTO> addNewCreatorRole(@RequestBody AddCreatorRoleDTO dto) {
        CreatorRoleResponseDTO savedRole = creatorRoleService.saveCreatorRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

}
