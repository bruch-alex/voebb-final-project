package com.example.voebb.service;

import com.example.voebb.model.entity.Country;

import java.util.List;

public interface CountryService {

    Country findOrCreate(String name);

    Country createCountry(Country country);

    Country getCountryById(Long id);

    Country updateCountry(Long id, Country updatedCountry);

    void deleteCountryById(Long id);

    List<Country> getAllCountries();

    List<Country> getCountriesByIds(List<Long> ids);

}
