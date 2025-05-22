package com.example.voebb.model.dto.creator;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatorWithRoleDTO {
    @NotBlank(message = "Creator first name is required")
    private String firstName;

    @NotBlank(message = "Creator last name is required")
    private String lastName;

    @NotBlank(message = "Creator role is required")
    private String role;
}