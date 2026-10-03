package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admissions", indexes = {
    @Index(name = "idx_adm_patient", columnList = "patientId"),
    @Index(name = "idx_adm_bed", columnList = "bedId"),
    @Index(name = "idx_adm_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    private Long departmentId;

    @Column(nullable = false)
    private Long wardId;

    @Column(nullable = false)
    private Long bedId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime admissionDate;

    @Column(length = 500)
    private String reason;

    @Column(length = 1000)
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdmissionStatus status; // ADMITTED, UNDER_TREATMENT, READY_FOR_DISCHARGE, DISCHARGED

    private LocalDateTime dischargeDate;
}
