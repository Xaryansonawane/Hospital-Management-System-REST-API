package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beds", indexes = {
    @Index(name = "idx_bed_ward_status", columnList = "wardId, status"),
    @Index(name = "idx_bed_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long wardId;

    @Column(nullable = false, length = 50)
    private String bedNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BedStatus status; // AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
}
