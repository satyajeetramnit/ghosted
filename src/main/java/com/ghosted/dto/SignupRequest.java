package com.ghosted.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Email
    @Size(max = 254)
    private String email;

    /**
     * Minimum 8 chars, must contain at least one letter and one digit.
     * Special characters are allowed but not required.
     */
    @NotBlank
    @Size(min = 8, max = 72) // 72 = bcrypt's max effective length
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
        message = "Password must contain at least one letter and one digit"
    )
    private String password;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;
}

