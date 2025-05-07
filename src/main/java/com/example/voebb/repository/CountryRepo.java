package com.example.voebb.repository;

import com.example.voebb.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepo extends JpaRepository<Country, Long> {
    Country findByName(String name);
    boolean existsByName(String name);
}
