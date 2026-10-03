package com.lifecare.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departments", indexes = {
    @Index(name = "idx_department_name", columnList = "name", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 20)
    private String status; // ACTIVE, INACTIVE
}
