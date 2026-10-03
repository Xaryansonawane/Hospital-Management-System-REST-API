package com.lifecare.hospital.dto.request;

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
public class CreatePrescriptionRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long consultationId;
    private String notes;

    @NotEmpty(message = "At least one prescription item is required")
    private List<PrescriptionItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrescriptionItemRequest {
        @NotNull(message = "Medicine ID is required")
        private Long medicineId;
        private String medicineName;
        private String dosage; // e.g. 500mg
        private String frequency; // e.g. 1-0-1
        private String duration; // e.g. 5 Days
        private String instructions; // e.g. After food
    }
}
