package com.example.voebb.model.dto.borrow;

import java.time.LocalDate;

public record GetBorrowingsDTO(
        Long borrowId,
        Long userId,
        String customUserFullName,
        Long itemId,
        String itemTitle,
        String productType,
        LocalDate startDate,
        LocalDate dueDate,
        LocalDate returnDate,
        int extendsCount,
        String borrowStatus  // [note: active , overdue, returned] calculated on-the-fly based on returnDate & dueDate
) {
}
