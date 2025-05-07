package com.example.voebb.service;

import com.example.voebb.model.entity.BookDetails;

public interface BookDetailsService {
    void saveDetails(BookDetails bookDetails);

    BookDetails getDetailsByProductId(Long productId);

    BookDetails updateDetails(Long productId, BookDetails newDetails);

    void deleteDetails(Long productId);
}
