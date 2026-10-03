package com.lifecare.hospital.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdmissionRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long departmentId;

    @NotNull(message = "Ward ID is required")
    private Long wardId;

    @NotNull(message = "Bed ID is required")
    private Long bedId;

    private String reason;
    private String diagnosis;
}
