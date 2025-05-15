package com.example.voebb.service.impl;

import com.example.voebb.model.dto.reservation.ReservationResponseDTO;
import com.example.voebb.repository.ReservationRepo;
import com.example.voebb.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;

    @Override
    public Page<ReservationResponseDTO> getFilteredReservations(Long clientId, Long itemId, Long libraryId, Pageable pageable) {
        return reservationRepo.findFilteredReservations(clientId, itemId, libraryId, pageable);
    }

}
