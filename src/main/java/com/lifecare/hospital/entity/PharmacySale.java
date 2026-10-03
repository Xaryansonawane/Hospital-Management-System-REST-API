package com.lifecare.hospital.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pharmacy_sales", indexes = {
    @Index(name = "idx_sale_patient", columnList = "patientId"),
    @Index(name = "idx_sale_presc", columnList = "prescriptionId"),
    @Index(name = "idx_sale_date", columnList = "saleDate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    private Long prescriptionId;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "pharmacySale", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private List<PharmacySaleItem> items = new ArrayList<>();

    public void addItem(PharmacySaleItem item) {
        items.add(item);
        item.setPharmacySale(this);
    }
}
