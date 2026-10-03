package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctors", indexes = {
    @Index(name = "idx_doctor_user_id", columnList = "userId"),
    @Index(name = "idx_doctor_department", columnList = "department"),
    @Index(name = "idx_doctor_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(length = 150)
    private String specialization;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String email;

    private Integer experience;

    @Column(precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(length = 255)
    private String availability; // e.g., "Mon-Fri 09:00-17:00"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DoctorStatus status;

    @Column(length = 500)
    private String doctorAvatar;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
