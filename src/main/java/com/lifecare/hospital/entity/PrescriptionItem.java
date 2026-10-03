package com.lifecare.hospital.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescription_items", indexes = {
    @Index(name = "idx_presc_item_presc", columnList = "prescription_id"),
    @Index(name = "idx_presc_item_med", columnList = "medicineId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    @JsonBackReference
    private Prescription prescription;

    @Column(nullable = false)
    private Long medicineId;

    @Column(length = 150)
    private String medicineName;

    @Column(nullable = false, length = 100)
    private String dosage; // e.g., 500mg

    @Column(nullable = false, length = 100)
    private String frequency; // e.g., 1-0-1

    @Column(nullable = false, length = 100)
    private String duration; // e.g., 5 Days

    @Column(length = 500)
    private String instructions; // e.g., After food
}
