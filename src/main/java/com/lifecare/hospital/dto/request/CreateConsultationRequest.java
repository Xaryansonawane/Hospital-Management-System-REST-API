package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.ConsultationSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsultationRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long appointmentId;
    private Long walkInVisitId;
    private Long emergencyCaseId;

    @NotNull(message = "Consultation source is required")
    private ConsultationSource source; // WALK_IN, EMERGENCY, APPOINTMENT

    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String vitals;
    private String notes;
    private LocalDate followUpDate;
}
