package com.example.voebb.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "creator_roles")
public class CreatorRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_role_id")
    private Long id;

    @Column(name = "creator_role", nullable = false, unique = true, length = 30)
    private String creatorRoleName;          // [AUTHOR, DIRECTOR, CO_AUTHOR etc. ]
}
