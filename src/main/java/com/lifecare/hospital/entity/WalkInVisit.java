package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "walk_in_visits", indexes = {
    @Index(name = "idx_walkin_token", columnList = "tokenNumber"),
    @Index(name = "idx_walkin_status", columnList = "status"),
    @Index(name = "idx_walkin_patient", columnList = "patientId"),
    @Index(name = "idx_walkin_doctor", columnList = "doctorId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkInVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String tokenNumber; // e.g. W-001

    @Column(nullable = false)
    private Long patientId;

    private Long departmentId;

    private Long doctorId;

    @Column(length = 500)
    private String visitReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    private LocalDateTime consultationStartTime;

    private LocalDateTime consultationEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WalkInStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
