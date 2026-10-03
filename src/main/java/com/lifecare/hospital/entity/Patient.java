package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients", indexes = {
    @Index(name = "idx_patient_code", columnList = "patientId", unique = true),
    @Index(name = "idx_patient_email", columnList = "email"),
    @Index(name = "idx_patient_phone", columnList = "phone")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String patientId; // PAT-2026-00001

    private Long userId; // optional link to User account

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    private LocalDate dateOfBirth;

    @Column(length = 20)
    private String gender;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String aadhar;

    @Column(length = 150)
    private String email;

    @Column(length = 500)
    private String address;

    @Column(length = 10)
    private String bloodGroup;

    @Column(length = 100)
    private String emergencyContact;

    @Column(length = 1000)
    private String allergies;

    @Column(length = 2000)
    private String medicalHistory;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
