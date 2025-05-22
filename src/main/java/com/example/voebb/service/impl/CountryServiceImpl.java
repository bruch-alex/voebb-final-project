package com.example.voebb.service.impl;

import com.example.voebb.model.entity.Country;
import com.example.voebb.repository.CountryRepo;
import com.example.voebb.service.CountryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;

    @Override
    @Transactional
    public Country findOrCreate(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Country name must be non-empty");
        }

        String sanitizedName = name.trim();

        return countryRepo.findByNameIgnoreCase(sanitizedName)
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setName(sanitizedName);
                    return countryRepo.save(c);
                });
    }

    @Override
    public Country createCountry(Country country) {
        if (countryRepo.existsByName(country.getName())) {
            throw new RuntimeException("Country with name " + country.getName() + " already exists");
        }
        return countryRepo.save(country);
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepo.findById(id).orElseThrow(() -> new RuntimeException("Country with Id" + id + "not found"));
    }


    @Override
    public Country updateCountry(Long id, Country updatedCountry) {
        Country existingCountry = getCountryById(id);
        existingCountry.setName(updatedCountry.getName());

        return countryRepo.save(existingCountry);
    }

    @Override
    public void deleteCountryById(Long id) {
        countryRepo.deleteById(id);

    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepo.findAll();
    }

    @Override
    public List<Country> getCountriesByIds(List<Long> ids) {
        return countryRepo.findAllById(ids);
    }

}
