package com.example.voebb.job;

import com.example.voebb.model.entity.Reservation;
import com.example.voebb.repository.ReservationRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationCleanupJob {

    private final ReservationRepo reservationRepo;
    private final Clock clock;

    // For testing replace with: @Scheduled(cron = "0 * * * * *") // every minute
    @Scheduled(cron = "0 0 2 * * *") // every day at 02:00 AM
    @Transactional
    public void removeExpiredReservations() {
        LocalDate today = LocalDate.now(clock); // predictable for testing
        List<Reservation> expired = reservationRepo.findByDueDateBefore(today);

        if (expired.isEmpty()) {
            log.info("No expired reservations to delete.");
            return;
        }

        expired.forEach(res -> log.warn("Deleting expired reservation ID {} for user {} (item ID {}), due date: {}",
                res.getId(),
                res.getCustomUser().getId(),
                res.getItem().getId(),
                res.getDueDate()));

        reservationRepo.deleteAll(expired);

        // TODO: decide to save the logs in a file
        log.info("Deleted {} expired reservations", expired.size());
    }
}
