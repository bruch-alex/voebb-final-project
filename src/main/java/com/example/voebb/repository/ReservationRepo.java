package com.example.voebb.repository;

import com.example.voebb.model.dto.reservation.GetReservationDTO;
import com.example.voebb.model.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT new com.example.voebb.model.dto.reservation.GetReservationDTO(
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
                WHERE (:userId IS NULL OR u.id = :userId)
                  AND (:itemId IS NULL OR i.id = :itemId)
                  AND (:libraryId IS NULL OR l.id = :libraryId)
            """)
    Page<GetReservationDTO> findFilteredReservations(
            @Param("userId") Long userId,
            @Param("itemId") Long itemId,
            @Param("libraryId") Long libraryId,
            Pageable pageable
    );

    int countByCustomUserId(Long userId);

}
