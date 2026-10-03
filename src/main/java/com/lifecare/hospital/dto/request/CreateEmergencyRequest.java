package com.lifecare.hospital.dto.request;

import com.lifecare.hospital.entity.EmergencyPriority;
import com.lifecare.hospital.entity.EmergencyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmergencyRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Chief Complaint is required")
    private String chiefComplaint;

    private String emergencyCondition;

    private EmergencyPriority priority; // CRITICAL, HIGH, MEDIUM, LOW

    private Long assignedDoctorId;

    private Long departmentId;

    private String bloodPressure;
    private Integer heartRate;
    private Integer oxygenSaturation;
    private Double temperature;
    private Integer respiratoryRate;
    private String treatmentNotes;
    private EmergencyStatus status;
}
