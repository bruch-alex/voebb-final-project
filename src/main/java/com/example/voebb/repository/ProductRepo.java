package com.example.voebb.repository;

import com.example.voebb.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = """
                   SELECT  distinct p
                   FROM Product p
                   LEFT JOIN ProductItem pi ON pi.product = p
                   LEFT JOIN ItemLocation il ON il.item = pi
                   LEFT JOIN Library lib ON il.library = lib
                   LEFT JOIN CreatorProductRelation cpr ON cpr.product = p
                   LEFT JOIN Creator c on cpr.creator = c
                   LEFT JOIN p.countries co
                   LEFT JOIN p.languages lang
                   WHERE   (:title is null or :title = ''  OR p.title ILIKE '%' || :title || '%')
                   AND     (:libraryId IS NULL OR lib.id = :libraryId)
                   AND     (:author IS NULL or :author = '' OR c.lastName ILIKE '%' || :author || '%' OR c.firstName ILIKE '%' || :author || '%')
                   AND     (:productType IS NULL OR p.type.id = :productType)
                   AND     (:languageId IS NULL OR lang.id = :languageId)
                   AND     (:countryId IS NULL OR co.id = :countryId)
                   """)
    Page<Product> searchWithFilters(@Param("title") String title,
                                    @Param("author") String author,
                                    @Param("libraryId") Long libraryId,
                                    @Param("productType") Long productType,
                                    @Param("languageId") Long languageId,
                                    @Param("countryId") Long countryId,
                                    Pageable pageable);

    Optional<Product> getProductById(Long id);

}
