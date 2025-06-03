package com.example.voebb.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
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


    @Column(name="phone_number", nullable = false, unique = true, length = 20)

    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    /**
     * Boolean wrapper lets Hibernate store null
     */
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true; // [active, inactive - related to membership]

    @Column(name = "borrowed_products_count", nullable = false)
    @Max(5)
    private Integer borrowedProductsCount = 0;

    // TODO: TBD add more fields => created_at(register_date), updated_at, expired_membership_date

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles_relation",
            joinColumns = @JoinColumn(name = "custom_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<CustomUserRole> roles = new HashSet<>();

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }

        // Remove all non-digit characters except the plus sign, so +4917612345678 remains
        String digitsOnly = phoneNumber.replaceAll("[^+\\d]", "");

        if (!digitsOnly.matches("^\\+49\\d{10,11}$")) {
            throw new IllegalArgumentException("Phone number must start with +49 followed by 10 or 11 digits");
        }

        // Now add hyphens: +49-XXX-XXXXXXX
        String withoutPrefix = digitsOnly.substring(3); // Remove '+49' prefix
        String providerCode = withoutPrefix.substring(0, 3);
        String subscriberNumber = withoutPrefix.substring(3);

        this.phoneNumber = "+49-" + providerCode + "-" + subscriberNumber;
    }
}
