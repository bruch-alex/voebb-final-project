package com.example.voebb.model.dto.reservation;

import java.time.LocalDate;

public record GetReservationDTO(
        Long id,
        Long userId,
        String customUserFullName,
        Long itemId,
        String itemTitle,
        LocalDate startDate,
        LocalDate dueDate,
        long daysUntilDue
) {}
