package com.example.voebb.service;

import com.example.voebb.model.entity.Country;

import java.util.List;

public interface CountryService {
    Country createCountry(Country country);

    Country getCountryById(Long id);
    List<Country> getAllCountries();
    Country updateCountry(Long id, Country updatedCountry);
    void deleteCountryById(Long id);
}
