package com.example.voebb.repository;

import com.example.voebb.model.dto.borrow.GetBorrowingsDTO;
import com.example.voebb.model.entity.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepo extends JpaRepository<Borrow, Long> {

    @Query("""
                SELECT new com.example.voebb.model.dto.borrow.GetBorrowingsDTO(
                    b.id,
                    u.id,
                    CONCAT(u.firstName, ' ', u.lastName),
                    i.id,
                    p.title,
                    p.type.name,
                    b.startDate,
                    b.dueDate,
                    b.returnDate,
                    b.extendsCount,
                    CASE
                        WHEN b.returnDate IS NOT NULL THEN 'Returned'
                        WHEN b.dueDate < CURRENT_DATE THEN 'Overdue'
                        ELSE 'Active'
                    END,
                    l.id
                )
                FROM Borrow b
                JOIN b.customUser u
                JOIN b.item i
                JOIN i.product p
                LEFT JOIN i.location loc
                LEFT JOIN loc.library l
                WHERE (:userId IS NULL OR u.id = :userId)
                  AND (:itemId IS NULL OR i.id = :itemId)
                  AND (
                     :libraryId IS NULL OR l.id = :libraryId
                   )
                  AND (
                    :status IS NULL OR
                    (:status = 'Returned' AND b.returnDate IS NOT NULL) OR
                    (:status = 'Overdue' AND b.returnDate IS NULL AND b.dueDate < CURRENT_DATE) OR
                    (:status = 'Active' AND b.returnDate IS NULL AND b.dueDate >= CURRENT_DATE)
                  )
            """)
    Page<GetBorrowingsDTO> findFilteredBorrows(
            @Param("userId") Long userId,
            @Param("itemId") Long itemId,
            @Param("libraryId") Long libraryId,
            @Param("status") String status,
            Pageable pageable
    );

    int countByCustomUserIdAndReturnDateIsNull(Long userId);

}
