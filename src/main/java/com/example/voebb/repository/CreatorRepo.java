package com.example.voebb.repository;

import com.example.voebb.model.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatorRepo extends JpaRepository<Creator, Long> {

    Optional<Creator> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    List<Creator> findTop5ByLastNameContainingIgnoreCase(String lastName);

}
