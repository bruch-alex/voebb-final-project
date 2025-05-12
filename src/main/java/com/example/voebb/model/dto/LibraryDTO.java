package com.example.voebb.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    private Long id;
    private String name;
    private String description;
    private String city;
    private String district;
    private String postcode;
    private String street;
    private String houseNr;
    private String osmLink;
}