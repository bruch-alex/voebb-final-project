package com.example.voebb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_types")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; //  [terms for BE logic : 'book, ebook, dvd, boardgame']

    @Column(name = "display_name", nullable = false)
    private String displayName; // [ label for UI: Book, E-Book, DVD, Board Game]

    /*
        14 days - films
        28 days - books
         7 days - boardgames
     */
    @Column(name = "borrow_duration_days", nullable = false)
    private int borrowDurationDays;

    @Column(name = "main_creator_role_id", nullable = false)
    private Long mainCreatorRoleId;
}
