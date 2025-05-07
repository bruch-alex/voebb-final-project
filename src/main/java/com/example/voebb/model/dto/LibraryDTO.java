package com.example.voebb.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    private Long id;
    
    @NotBlank(message = "Library name is required")
    @Size(min = 2, max = 100, message = "Library name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;
    
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    private String openingHours;
    private String contactInfo;
    private String email;
    private String phone;
    private String website;
}
