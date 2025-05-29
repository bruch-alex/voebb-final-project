package com.example.voebb.service;

import com.example.voebb.model.dto.borrow.CreateBorrowDTO;
import com.example.voebb.model.dto.borrow.GetBorrowingsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowService {

    void createBorrow(CreateBorrowDTO dto);

    Page<GetBorrowingsDTO> getFilteredBorrowings(Long userId, Long itemId, Long libraryId, String status, Pageable pageable);

    String returnBorrow(Long borrowId);

    String extendBorrow(Long borrowId);
}
