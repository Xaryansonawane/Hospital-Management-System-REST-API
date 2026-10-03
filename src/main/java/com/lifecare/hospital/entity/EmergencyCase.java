package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_cases", indexes = {
    @Index(name = "idx_emergency_priority", columnList = "priority"),
    @Index(name = "idx_emergency_status", columnList = "status"),
    @Index(name = "idx_emergency_patient", columnList = "patientId"),
    @Index(name = "idx_emergency_doctor", columnList = "assignedDoctorId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false, length = 500)
    private String chiefComplaint;

    @Column(length = 500)
    private String emergencyCondition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmergencyPriority priority;

    private Long assignedDoctorId;

    private Long departmentId;

    @Column(length = 20)
    private String bloodPressure;

    private Integer heartRate;

    private Integer oxygenSaturation;

    private Double temperature;

    private Integer respiratoryRate;

    @Column(length = 2000)
    private String treatmentNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmergencyStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
