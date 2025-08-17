package com.swarga.Kartwala.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50, message = "The username should contain between 3 to 50 characters!!")
    private String username;
    @NotBlank
    @Email(message = "Please enter a valid email id!!")
    private String email;
    private Set<String> roles;

    @NotBlank
    @Size(min = 8, max = 40, message = "The password should contain between 8 to 40 characters!!")
    private String password;
}
