package com.example.voebb.service;

import com.example.voebb.model.dto.reservation.ReservationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ReservationService {

    public Page<ReservationResponseDTO> getFilteredReservations(Long clientId, Long itemId, Long libraryId, Pageable pageable);
}
