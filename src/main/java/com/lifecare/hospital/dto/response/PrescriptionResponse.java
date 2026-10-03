package com.lifecare.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long consultationId;
    private LocalDateTime prescriptionDate;
    private String notes;
    private List<PrescriptionItemResponse> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrescriptionItemResponse {
        private Long id;
        private Long medicineId;
        private String medicineName;
        private String dosage;
        private String frequency;
        private String duration;
        private String instructions;
    }
}
