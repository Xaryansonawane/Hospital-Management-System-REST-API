package com.lifecare.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {
    private Long id;
    private String medicineName;
    private String category;
    private String manufacturer;
    private String batchNumber;
    private LocalDate expiryDate;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private Integer minimumStockLevel;
    private Boolean prescriptionRequired;
    private Boolean isLowStock;
    private Boolean isExpired;
}
