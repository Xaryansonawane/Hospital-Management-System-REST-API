package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments", indexes = {
    @Index(name = "idx_appt_doctor_slot", columnList = "doctorId, appointmentDate, appointmentTime"),
    @Index(name = "idx_appt_patient", columnList = "patientId"),
    @Index(name = "idx_appt_status", columnList = "status"),
    @Index(name = "idx_appt_date", columnList = "appointmentDate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    private Long departmentId;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Column(length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AppointmentStatus status;

    @Column(length = 2000)
    private String notes;

    // Backward-compatibility attributes for existing React frontend
    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String aadhar;

    private LocalDate dob;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String doctorFirstName;

    @Column(length = 100)
    private String doctorLastName;

    @Column(length = 500)
    private String address;

    @Builder.Default
    private Boolean hasVisited = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
