package com.example.voebb.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(name = "address_city", nullable = false, length = 90)
    private String city;

    @Column(name = "address_district")
    private String district;

    @Column(name = "address_postcode", length = 10)
    private String postcode;

    @Column(name = "address_street", nullable = false, length = 120)
    private String street;

    @Column(name = "address_house_nr", nullable = false, length = 15)
    private String houseNr;          // e.g “15A”, “12-B”

    @Column(name = "address_osm_link", length = 255)
    private String osmLink;
}