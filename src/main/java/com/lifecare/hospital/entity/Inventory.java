package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", indexes = {
    @Index(name = "idx_inv_medicine", columnList = "medicineId", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long medicineId;

    @Column(nullable = false)
    private Integer quantityAvailable;

    @Column(length = 100)
    private String location; // e.g., "Shelf A-3"

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
