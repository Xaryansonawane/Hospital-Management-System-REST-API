package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySaleResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long prescriptionId;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private List<PharmacySaleItemResponse> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PharmacySaleItemResponse {
        private Long id;
        private Long medicineId;
        private String medicineName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}
