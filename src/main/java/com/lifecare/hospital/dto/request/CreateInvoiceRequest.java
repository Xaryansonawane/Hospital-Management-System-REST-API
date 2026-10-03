package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.ServiceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotEmpty(message = "At least one item is required")
    private List<InvoiceItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceItemRequest {
        @NotNull(message = "Service type is required")
        private ServiceType serviceType;

        @NotNull(message = "Description is required")
        private String description;

        @NotNull(message = "Quantity is required")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        private BigDecimal unitPrice;
    }
}
