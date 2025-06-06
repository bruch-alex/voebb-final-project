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

import java.util.List;

@Repository
public interface LibraryRepo extends JpaRepository<Library, Long> {

    @Query("""
       SELECT l FROM Library l
       WHERE (:libraryId IS NULL OR l.id = :libraryId)
       AND (:name IS NULL OR :name = '' OR l.name ILIKE '%' || :name || '%')
       AND (:district IS NULL OR :district = '' OR l.address.district ILIKE '%' || :district || '%')
       """)
    Page<Library> findFilteredLibrariesForAdmin(
            @Param("libraryId") Long libraryId,
            @Param("name") String name,
            @Param("district") String district,
            Pageable pageable);



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

    @Query("""
               SELECT new com.example.voebb.model.dto.library.LibraryDTO(
                   l.id,
                   l.address.district,
                   l.name
               )
               FROM Library l
               ORDER BY l.address.district
           """)
    List<LibraryDTO> getAllInDTO();

    boolean existsByName(String name);

    @Query("SELECT DISTINCT l.address.district FROM Library l WHERE l.address.district IS NOT NULL ORDER BY l.address.district")
    List<String> findAllDistricts();

}