package com.example.voebb.service.impl;

import com.example.voebb.model.dto.product.BookDetailsDTO;
import com.example.voebb.model.dto.product.NewBookDetailsDTO;
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
    public void saveBookDetails(NewBookDetailsDTO dto, Product product) {
        BookDetails details = new BookDetails();
        details.setIsbn(dto.getIsbn());
        details.setEdition(dto.getEdition());
        details.setPages(dto.getPages());
        details.setProduct(product);

        bookDetailsRepo.save(details);
    }


    @Override
    public BookDetailsDTO getDetailsDTOByProductId(Long productId) {
        Product product = productRepo.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getType() == null) return null;

        String type = product.getType().getName();
        if (!"book".equalsIgnoreCase(type) && !"ebook".equalsIgnoreCase(type)) {
            return null;
        }

        BookDetails details = product.getBookDetails();
        if (details == null) return null;

        return new BookDetailsDTO(
                details.getIsbn(),
                details.getEdition(),
                details.getPages()
        );
    }


    @Override
    public BookDetails updateDetails(Long productId, NewBookDetailsDTO newDetails) {
        BookDetails existing = bookDetailsRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("BookDetails not found for product ID: " + productId));

        existing.setEdition(newDetails.getEdition());
        existing.setIsbn(newDetails.getIsbn());
        existing.setPages(newDetails.getPages());

        return bookDetailsRepo.save(existing);
    }

    @Override
    public void deleteDetails(Long productId) {
        if (!bookDetailsRepo.existsById(productId)) {
            throw new RuntimeException("BookDetails not found for product ID: " + productId);
        }

        bookDetailsRepo.deleteById(productId);
    }

    @Override
    public void updateOrCreateBookDetails(BookDetailsDTO bookDetailsDTO, Product existingProduct) {
        BookDetails bookDetails = existingProduct.getBookDetails(); // ✅ fixed

        if (bookDetails == null) {
            bookDetails = new BookDetails();
            bookDetails.setProduct(existingProduct); // ✅ use existingProduct here too
        }

        bookDetails.setIsbn(bookDetailsDTO.getIsbn());
        bookDetails.setEdition(bookDetailsDTO.getEdition());
        bookDetails.setPages(bookDetailsDTO.getPages());

        bookDetailsRepo.save(bookDetails);
    }


}
