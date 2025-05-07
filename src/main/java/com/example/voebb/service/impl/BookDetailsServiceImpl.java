package com.example.voebb.service.impl;

import com.example.voebb.model.entity.BookDetails;
import com.example.voebb.model.entity.Product;
import com.example.voebb.repository.BookDetailsRepo;
import com.example.voebb.repository.ProductRepo;
import com.example.voebb.service.BookDetailsService;
import org.springframework.stereotype.Service;

@Service
public class BookDetailsServiceImpl implements BookDetailsService {

    private final BookDetailsRepo bookDetailsRepo;
    private final ProductRepo productRepo;

    public BookDetailsServiceImpl(BookDetailsRepo bookDetailsRepo, ProductRepo productRepo) {
        this.bookDetailsRepo = bookDetailsRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void saveDetails(BookDetails bookDetails) {
        bookDetailsRepo.save(bookDetails);
    }

    @Override
    public BookDetails getDetailsByProductId(Long productId) {
        Product product = productRepo.getProductById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        String productType = product.getType().getName();

        if (!productType.equals("book") || !productType.equals("ebook")) {
            throw new RuntimeException("Product is not a book");
        }

        return product.getBookDetails();
    }

    @Override
    public BookDetails updateDetails(Long productId, BookDetails newDetails) {
        BookDetails oldDetails = getDetailsByProductId(productId);

        oldDetails.setEdition(newDetails.getEdition());
        oldDetails.setIsbn(newDetails.getIsbn());
        oldDetails.setPages(newDetails.getPages());


        return bookDetailsRepo.save(oldDetails);
    }

    @Override
    public void deleteDetails(Long productId) {
        getDetailsByProductId(productId);
        bookDetailsRepo.deleteById(productId);
    }
}
