package com.example.voebb.repository;

import com.example.voebb.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Page<Product> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);

    Optional<Product> getProductById(Long id);
}
