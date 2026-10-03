package com.lifecare.hospital.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CreateMedicineRequest {

    @NotBlank(message = "Medicine Name is required")
    private String medicineName;

    private String category;
    private String manufacturer;
    private String batchNumber;

    @NotNull(message = "Expiry Date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Purchase price is required")
    @Positive(message = "Purchase price must be positive")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @Positive(message = "Selling price must be positive")
    private BigDecimal sellingPrice;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Minimum stock level is required")
    private Integer minimumStockLevel;

    private Boolean prescriptionRequired;
}
