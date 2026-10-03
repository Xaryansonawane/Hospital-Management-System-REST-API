package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations", indexes = {
    @Index(name = "idx_consult_patient", columnList = "patientId"),
    @Index(name = "idx_consult_doctor", columnList = "doctorId"),
    @Index(name = "idx_consult_source", columnList = "source")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    private Long appointmentId;

    private Long walkInVisitId;

    private Long emergencyCaseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConsultationSource source;

    @Column(length = 2000)
    private String symptoms;

    @Column(nullable = false, length = 2000)
    private String diagnosis;

    @Column(length = 500)
    private String vitals;

    @Column(length = 2000)
    private String notes;

    private LocalDate followUpDate;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime consultationDate;
}
