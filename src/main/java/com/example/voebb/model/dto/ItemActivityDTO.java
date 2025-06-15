package com.example.voebb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemActivityDTO {
    private Long activityId; // reservation_id or borrowing_id
    private String activityType;
    private Integer borrowExtendsCount;

    private String itemTitle;
    private Long itemId;

    private LocalDate startDate;
    private LocalDate dueDate;

    private Boolean expiresSoon;

    private Long libraryId;
}
