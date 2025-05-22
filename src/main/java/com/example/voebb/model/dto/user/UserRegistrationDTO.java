package com.example.voebb.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(

        @NotNull(message = "Email must be not null")
        @Email(message = "Email should match pattern")
        String email,

        @NotNull(message = "Phone Number must be not null")
        @Pattern(regexp = "^\\+[0-9]{10,15}$", message = "Invalid phone number format")
        String phoneNumber,

        @NotNull(message = "Password must be not null")
        @Size(min = 8, message = "Password must be at least 8 chars long")
        String password,

        @NotNull(message = "First name must be not null")
        String firstName,

        @NotNull(message = "Last name must be not null")
        String lastName) {
}
