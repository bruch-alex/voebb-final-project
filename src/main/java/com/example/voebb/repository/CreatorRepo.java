package com.example.voebb.repository;

import com.example.voebb.model.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatorRepo extends JpaRepository<Creator, Long> {

    Optional<Creator> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    List<Creator> findTop5ByLastNameContainingIgnoreCase(String lastName);

    @Query(value = """
                SELECT * FROM creators
                WHERE LOWER(creator_first_name) ILIKE CONCAT('%', :name, '%')
                   OR LOWER(creator_last_name) ILIKE CONCAT('%', :name, '%')
                LIMIT 5
            """, nativeQuery = true)
    List<Creator> searchByNameNative(@Param("name") String name);

}
