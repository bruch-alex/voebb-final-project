package com.example.voebb.model.dto.creator;

public record CreatorWithRoleDTO(
        Long creatorId,
        String firstName,
        String lastName,
        Long roleId,
        String roleName
) {}
