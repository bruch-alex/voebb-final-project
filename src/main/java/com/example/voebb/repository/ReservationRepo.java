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
import java.util.List;


@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    @Query("""
                SELECT new com.example.voebb.model.dto.reservation.GetReservationDTO(
                    r.id,
                    u.id,
                    CONCAT(u.firstName, ' ', u.lastName),
                    i.id,
                    p.title,
                    r.startDate,
                    r.dueDate,
                    0L                  )
                FROM Reservation r
                JOIN r.customUser u
                JOIN r.item i
                JOIN i.product p
                LEFT JOIN i.location loc
                LEFT JOIN loc.library l
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

    List<Reservation> findByDueDateBefore(LocalDate today);

}
