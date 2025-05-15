package com.example.voebb.repository;

import com.example.voebb.model.dto.library.EditLibraryDTO;
import com.example.voebb.model.dto.library.LibraryDTO;
import com.example.voebb.model.entity.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepo extends JpaRepository<Library, Long> {
    @Query(value = """
                SELECT new com.example.voebb.model.dto.library.LibraryDTO(
                    l.id,
                    l.address.district,
                    l.name
                )
                FROM Library l
            """,
            countQuery = """
                        SELECT COUNT(l)
                        FROM Library l
                    """)
    Page<LibraryDTO> getLibrariesForAdmin(Pageable pageable);

    @Query("""
                SELECT new com.example.voebb.model.dto.library.EditLibraryDTO(
                    l.id,
                    l.name,
                    l.description,
                    l.address.postcode,
                    l.address.city,
                    l.address.district,            
                    l.address.street,    
                    l.address.houseNr,
                    l.address.osmLink
                )
                FROM Library l
                WHERE l.id = :id
            """)
    EditLibraryDTO getLibraryFullInfo(@Param("id") Long id);
}