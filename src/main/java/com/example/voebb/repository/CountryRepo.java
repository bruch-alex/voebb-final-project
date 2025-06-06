package com.example.voebb.repository;

import com.example.voebb.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CountryRepo extends JpaRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String name);

    List<Country> findByNameIn(List<String> names);

    boolean existsByName(String name);
}
