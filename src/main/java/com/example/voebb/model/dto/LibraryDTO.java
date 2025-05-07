package com.example.voebb.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    // Constructors
    public LibraryDTO() {
    }

    public LibraryDTO(Long id, String name, String location, String address, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.address = address;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}