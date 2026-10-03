package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePharmacySaleRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long prescriptionId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotEmpty(message = "At least one sale item is required")
    private List<SaleItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleItemRequest {
        @NotNull(message = "Medicine ID is required")
        private Long medicineId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
