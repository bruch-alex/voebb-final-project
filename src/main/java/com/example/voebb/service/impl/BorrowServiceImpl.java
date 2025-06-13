package com.example.voebb.service.impl;

import com.example.voebb.exception.*;
import com.example.voebb.model.dto.borrow.CreateBorrowDTO;
import com.example.voebb.model.dto.borrow.GetBorrowingsDTO;
import com.example.voebb.model.entity.Borrow;
import com.example.voebb.model.entity.CustomUser;
import com.example.voebb.model.entity.ItemStatus;
import com.example.voebb.model.entity.ProductItem;
import com.example.voebb.repository.*;
import com.example.voebb.service.BorrowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private static final int BORROW_EXTENSION_DAYS = 14;

    private final BorrowRepo borrowRepo;
    private final CustomUserRepo userRepo;
    private final ProductItemRepo itemRepo;
    private final ItemStatusRepo statusRepo;
    private final ReservationRepo reservationRepo;

    @Override
    @Transactional
    public String createBorrow(CreateBorrowDTO dto) {

        if (dto.userId() == null || dto.itemId() == null) {
            throw new IllegalArgumentException("Both User ID and Item ID are required.");
        }

        CustomUser user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));
        ProductItem item = itemRepo.findById(dto.itemId())
                .orElseThrow(() -> new ItemNotFoundException(dto.itemId()));

        return createBorrowInternal(user, item, false);
    }


    @Override
    public Page<GetBorrowingsDTO> getFilteredBorrowings(Long userId, Long itemId, Long libraryId, String status, Pageable pageable) {
        return borrowRepo.findFilteredBorrows(userId, itemId, libraryId, status, pageable);
    }

    @Override
    @Transactional
    public String returnBorrow(Long borrowId) {
        Borrow borrow = borrowRepo.findById(borrowId)
                .orElseThrow(() -> new BorrowNotFoundException(borrowId));

        if (borrow.getReturnDate() != null) {
            throw new IllegalStateException("This item is already returned.");
        }


        borrow.setReturnDate(LocalDate.now());
        borrowRepo.save(borrow);

        ProductItem item = borrow.getItem();
        ItemStatus availableStatus = statusRepo.findByNameIgnoreCase("available")
                .orElseThrow(() -> new ItemStatusNotFoundException("available"));
        item.setStatus(availableStatus);
        itemRepo.save(item);

        CustomUser user = borrow.getCustomUser();
        user.setBorrowedProductsCount(Math.max(0, user.getBorrowedProductsCount() - 1));
        userRepo.save(user);

        String userName = user.getFirstName() + " " + user.getLastName();
        String productTitle = item.getProduct().getTitle();

        return "[User ID: #" + user.getId() + "] " + userName +
               " successfully returned the item \"" + productTitle + "\" [Item ID: #" + item.getId() + "].";

    }

    @Override
    @Transactional
    public String extendBorrow(Long borrowId) {
        Borrow borrow = borrowRepo.findById(borrowId)
                .orElseThrow(() -> new BorrowNotFoundException(borrowId));

        if (borrow.getReturnDate() != null) {
            throw new RuntimeException("Returned items cannot be extended.");
        }

        if (borrow.getExtendsCount() >= 2) {
            throw new BorrowExtensionLimitReachedException(borrowId);
        }

        borrow.setDueDate(borrow.getDueDate().plusDays(BORROW_EXTENSION_DAYS));
        borrow.setExtendsCount(borrow.getExtendsCount() + 1);
        borrowRepo.save(borrow);

        ProductItem item = borrow.getItem();
        CustomUser user = borrow.getCustomUser();
        String userName = user.getFirstName() + " " + user.getLastName();
        String productTitle = item.getProduct().getTitle();

        return "[User ID: " + user.getId() + "] " + userName +
               " successfully extended the item \"" + productTitle + "\" [Item ID: " + item.getId() + "] until " +
               borrow.getDueDate() + ".";
    }

    public String createBorrowInternal(CustomUser user, ProductItem item, boolean skipAvailabilityAndLimitCheck) {

        if (!skipAvailabilityAndLimitCheck) {
            if (!item.getStatus().getName().equalsIgnoreCase("available")) {
                throw new ItemNotAvailableException(item.getProduct().getTitle(), item.getId(), item.getStatus().getName());
            }

            int activeBorrowCount = borrowRepo.countByCustomUserIdAndReturnDateIsNull(user.getId());
            int activeReservationCount = reservationRepo.countByCustomUserId(user.getId());

            if (activeBorrowCount + activeReservationCount >= 5) {
                throw new UserBorrowLimitExceededException(user.getId(), user.getFirstName(), user.getLastName());
            }
        }

        if (item.getProduct() == null || item.getProduct().getType() == null) {
            throw new IllegalStateException("Item's product or product type is not properly set.");
        }

        int borrowDurationDays = item.getProduct().getType().getBorrowDurationDays();
        LocalDate startDate = LocalDate.now();
        LocalDate dueDate = startDate.plusDays(borrowDurationDays);

        Borrow borrow = new Borrow();
        borrow.setCustomUser(user);
        borrow.setItem(item);
        borrow.setStartDate(startDate);
        borrow.setDueDate(dueDate);
        borrow.setExtendsCount(0);
        borrowRepo.save(borrow);

        ItemStatus borrowedStatus = statusRepo.findByNameIgnoreCase("borrowed")
                .orElseThrow(() -> new ItemStatusNotFoundException("borrowed"));
        item.setStatus(borrowedStatus);
        itemRepo.save(item);

        user.setBorrowedProductsCount(user.getBorrowedProductsCount() + 1);
        userRepo.save(user);

        return "Item \"" + item.getProduct().getTitle() + "\" [ID: " + item.getId() + "] was borrowed by User [ID: " + user.getId() + "] " + user.getFirstName() + " " + user.getLastName() + ".";
    }


}
