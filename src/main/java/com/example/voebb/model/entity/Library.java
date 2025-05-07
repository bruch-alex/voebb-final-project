package com.example.voebb.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_id")
    private Long id;
    
    @Column(name = "library_name", nullable = false, length = 120)
    private String name;
    
    @Column(name = "library_description")
    private String description; // e.g: note about entrance
    
    @Column(name = "location", nullable = false, length = 100)
    private String location;
    
    @Column(name = "street_address", length = 255)
    private String address;
    
    @Column(name = "opening_hours")
    private String openingHours;
    
    @Column(name = "contact_info")
    private String contactInfo;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "website")
    private String website;
}
