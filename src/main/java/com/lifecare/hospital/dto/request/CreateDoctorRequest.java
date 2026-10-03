package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.DoctorStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorRequest {
    private Long userId;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    private String specialization;

    @NotBlank(message = "Department is required")
    private String department;

    private String phone;

    @Email(message = "Valid email is required")
    private String email;

    private Integer experience;
    private BigDecimal consultationFee;
    private String availability;
    private DoctorStatus status;
    private String doctorAvatar;
}
