package com.lifecare.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String patientId; // PAT-2026-00001
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String aadhar;
    private String email;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private String allergies;
    private String medicalHistory;
    private LocalDateTime createdAt;
}
