package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First Name is required")
    @Size(min = 3, message = "First Name must contain at least 3 characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 2, message = "Last Name must contain at least 2 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid Email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number must contain exactly 10 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Aadhar number is required")
    @Size(min = 12, max = 12, message = "Aadhar number must contain exactly 12 digits")
    private String aadhar;

    @NotNull(message = "Date of Birth is required")
    private LocalDate dob;

    private Role role; // Defaults to PATIENT if null

    private String doctrDptmnt; // For doctor registration
    private String doctorAvatar;
}
