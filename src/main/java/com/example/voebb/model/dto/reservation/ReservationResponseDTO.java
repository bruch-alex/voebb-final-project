package com.example.voebb.model.dto.reservation;

import java.time.LocalDate;

public record ReservationResponseDTO(
        Long id,
        String customUserFullName,
        String itemTitle,
        LocalDate startDate,
        LocalDate dueDate
) {
}