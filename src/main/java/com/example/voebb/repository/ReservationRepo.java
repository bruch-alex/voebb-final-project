package com.example.voebb.repository;

import com.example.voebb.model.dto.reservation.ReservationResponseDTO;
import com.example.voebb.model.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT new com.example.voebb.model.dto.reservation.ReservationResponseDTO(
                    r.id,
                    CONCAT(u.firstName, ' ', u.lastName),
                    p.title,
                    r.startDate,
                    r.dueDate
                )
                FROM Reservation r
                JOIN r.customUser u
                JOIN r.item i
                JOIN i.product p
                JOIN i.location loc
                JOIN loc.library l
                WHERE (:clientId IS NULL OR u.id = :clientId)
                  AND (:itemId IS NULL OR i.id = :itemId)
                  AND (:libraryId IS NULL OR l.id = :libraryId)
            """)
    Page<ReservationResponseDTO> findFilteredReservations(
            @Param("clientId") Long clientId,
            @Param("itemId") Long itemId,
            @Param("libraryId") Long libraryId,
            Pageable pageable
    );

}
