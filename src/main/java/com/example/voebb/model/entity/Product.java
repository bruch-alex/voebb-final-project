package com.example.voebb.model.entity;

import com.example.voebb.model.dto.product.BookDetailsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "product_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType type;


    @Column(nullable = false)
    private String title;

    @Column(name = "release_year")
    private String releaseYear;

    private String photo;

    @Column(columnDefinition = "text")
    private String description; // summary of the product based on media_type

    /**
     * Non-NULL for digital products (ebook)
     * NULL for physical ones (book, dvd, board-game)
     */
    @Column(name = "product_link_to_emedia")
    private String productLinkToEmedia;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookDetails bookDetails;


    @ManyToMany
    @JoinTable(
            name = "language_relation",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private List<Language> languages;

    @ManyToMany
    @JoinTable(
            name = "country_relation",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<Country> countries;

    @OneToMany(mappedBy = "product")
    List<CreatorProductRelation> creatorProductRelations = new ArrayList<>();

    public boolean isBook(){
       return this.getType().getName().equalsIgnoreCase("book") || this.getType().getName().equalsIgnoreCase("ebook");
    }

    public void addRelation(CreatorProductRelation creatorProductRelation){
        creatorProductRelations.add(creatorProductRelation);
        creatorProductRelation.setProduct(this);
    }

}