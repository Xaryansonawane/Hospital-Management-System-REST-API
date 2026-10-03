package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWalkInRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long departmentId;

    private Long doctorId;

    private String visitReason;

    private Priority priority; // LOW, MEDIUM, HIGH (Defaults to MEDIUM)
}
