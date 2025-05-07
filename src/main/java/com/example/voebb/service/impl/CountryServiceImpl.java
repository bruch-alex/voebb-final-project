package com.example.voebb.service.impl;

import com.example.voebb.model.entity.Country;
import com.example.voebb.repository.CountryRepo;
import com.example.voebb.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;

    @Autowired
    public CountryServiceImpl(CountryRepo countryRepo) {
        this.countryRepo = countryRepo;
    }

    @Override
    public Country createCountry (Country country) {
        if(countryRepo.existsByName(country.getName())){
            throw new RuntimeException("Country with name " + country.getName() + " already exists");
        }
        return countryRepo.save(country);
    }

    @Override
    public Country getCountryById(Long id) {
        return countryRepo.findById(id).orElseThrow(()->new RuntimeException("Country with Id" + id +  "not found"));
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepo.findAll();
    }

    @Override
    public Country updateCountry(Long id, Country updatedCountry) {
        Country existingCountry = getCountryById(id);
        existingCountry.setName(updatedCountry.getName());

        return  countryRepo.save(existingCountry);
    }

    @Override
    public void deleteCountryById(Long id) {
        countryRepo.deleteById(id);

    }


}
