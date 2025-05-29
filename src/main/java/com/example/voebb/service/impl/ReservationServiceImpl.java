package com.example.voebb.service.impl;

import com.example.voebb.exception.*;
import com.example.voebb.model.dto.reservation.CreateReservationDTO;
import com.example.voebb.model.dto.reservation.GetReservationDTO;
import com.example.voebb.model.entity.CustomUser;
import com.example.voebb.model.entity.ItemStatus;
import com.example.voebb.model.entity.ProductItem;
import com.example.voebb.model.entity.Reservation;
import com.example.voebb.repository.*;
import com.example.voebb.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final CustomUserRepo userRepo;
    private final ProductItemRepo itemRepo;
    private final ItemStatusRepo statusRepo;
    private final BorrowRepo borrowRepo;


    @Override
    public Page<GetReservationDTO> getFilteredReservations(Long userId, Long itemId, Long libraryId, Pageable pageable) {
        return reservationRepo.findFilteredReservations(userId, itemId, libraryId, pageable);
    }


    @Override
    @Transactional
    public void createReservation(CreateReservationDTO dto) {

        if (dto.userId() == null || dto.itemId() == null) {
            throw new IllegalArgumentException("Both User ID and Item ID are required.");
        }

        CustomUser user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));

        ProductItem item = itemRepo.findById(dto.itemId())
                .orElseThrow(() -> new ItemNotFoundException(dto.itemId()));

        if (!item.getStatus().getName().equalsIgnoreCase("available")) {
            throw new ItemNotAvailableException(item.getProduct().getTitle(), item.getId(), item.getStatus().getName());
        }

        int activeBorrowCount = borrowRepo.countByCustomUserIdAndReturnDateIsNull(user.getId());
        int activeReservationCount = reservationRepo.countByCustomUserId(user.getId());

        if (activeBorrowCount + activeReservationCount >= 5) {
            throw new UserBorrowLimitExceededException(user.getId(), user.getFirstName(), user.getLastName());
        }

        Reservation reservation = new Reservation();
        reservation.setCustomUser(user);
        reservation.setItem(item);
        reservation.setStartDate(LocalDate.now());
        reservation.setDueDate(LocalDate.now().plusDays(3));

        reservationRepo.save(reservation);

        ItemStatus reservedStatus = statusRepo.findByNameIgnoreCase("reserved")
                .orElseThrow(() -> new ItemStatusNotFoundException("reserved"));
        item.setStatus(reservedStatus);
        itemRepo.save(item);

        user.setBorrowedProductsCount(user.getBorrowedProductsCount() + 1);
        userRepo.save(user);
    }

    @Override
    @Transactional
    // TODO: decide what should be updated real-life scenario
    public void updateReservation(Long id, CreateReservationDTO dto) {
        Reservation reservation = reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        CustomUser user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));

        ProductItem item = itemRepo.findById(dto.itemId())
                .orElseThrow(() -> new ItemNotFoundException(dto.itemId()));

        reservation.setCustomUser(user);
        reservation.setItem(item);
    }


    @Override
    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        ItemStatus available = statusRepo.findByNameIgnoreCase("available")
                .orElseThrow(() -> new ItemStatusNotFoundException("available"));
        reservation.getItem().setStatus(available);

        CustomUser user = reservation.getCustomUser();
        user.setBorrowedProductsCount(user.getBorrowedProductsCount() - 1);

        userRepo.save(user);
        itemRepo.save(reservation.getItem());
        reservationRepo.delete(reservation);
    }

}
