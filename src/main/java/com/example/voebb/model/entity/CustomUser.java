package com.example.voebb.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "custom_users")
public class CustomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //TODO: TBD => migrate to UUID type
    @Column(name = "custom_user_id")
    private Long id;

    @Column(name = "first_name")//, nullable = false, length = 60)
    private String firstName;

    @Column(name = "last_name")//, nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * Boolean wrapper lets Hibernate store null
     */
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true; // [active, inactive - related to membership]

    @Column(name = "borrowed_books_count", nullable = false)
    @Max(5)
    private Integer borrowedBooksCount = 0;

    // TODO: TBD add more fields => created_at(register_date), updated_at, expired_membership_date

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles_relation",
            joinColumns = @JoinColumn(name = "custom_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<CustomUserRole> roles = new HashSet<>();

}
