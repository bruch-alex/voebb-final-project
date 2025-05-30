package com.example.voebb.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private String name; // e.g. [note: 'Book, Ebook, DVD, Boardgame']

    // TODO: TBD
    //    @Column(name = "borrow_duration_days", nullable = false)
    //    private int borrowDurationDays; // [note: '14 days for films and 28 for books']

    @Column(name = "main_creator_role_id", nullable = false)
    private Long mainCreatorRoleId;

    @Column(name = "default_cover_url")
    private String defaultCoverUrl;
}
