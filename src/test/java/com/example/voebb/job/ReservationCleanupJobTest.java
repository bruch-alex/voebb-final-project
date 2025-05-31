package com.example.voebb.job;

import com.example.voebb.config.TestClockConfig;
import com.example.voebb.model.entity.CustomUser;
import com.example.voebb.model.entity.ProductItem;
import com.example.voebb.model.entity.Reservation;
import com.example.voebb.repository.CustomUserRepo;
import com.example.voebb.repository.ProductItemRepo;
import com.example.voebb.repository.ReservationRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties", properties = "spring.profiles.active=test")
@Import(TestClockConfig.class)
class ReservationCleanupJobTest {

    @Autowired
    private ReservationCleanupJob cleanupJob;

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private CustomUserRepo userRepo;

    @Autowired
    private ProductItemRepo itemRepo;

    @Autowired
    Clock clock;

    @Test
    void testRemoveExpiredReservations() {

        // Arrange
        CustomUser user = new CustomUser();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setPhoneNumber("+1234567890123451");
        user.setPassword("password");
        user.setEnabled(true);
        user.setBorrowedProductsCount(0);
        userRepo.save(user);

        ProductItem item = new ProductItem();
        itemRepo.save(item);

        LocalDate fixedToday = LocalDate.now(clock); // 2025-05-25

        Reservation expiredRes1 = new Reservation(null, user, item, fixedToday.minusDays(5), fixedToday.minusDays(2));
        Reservation expiredRes2 = new Reservation(null, user, item, fixedToday.minusDays(4), fixedToday.minusDays(1));
        Reservation validRes1 = new Reservation(null, user, item, fixedToday, fixedToday.plusDays(3));
        Reservation validRes3 = new Reservation(null, user, item, fixedToday.minusDays(1), fixedToday.plusDays(2));

        reservationRepo.saveAll(List.of(expiredRes1, expiredRes2, validRes1, validRes3));

        // Act
        cleanupJob.removeExpiredReservations();

        // Assert
        List<Reservation> remaining = reservationRepo.findAll();
        assertThat(remaining).allMatch(r -> !r.getDueDate().isBefore(fixedToday));
        assertThat(remaining).hasSize(2);
    }

}