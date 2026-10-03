package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.EmergencyPriority;
import com.lifecare.hospital.entity.EmergencyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private LocalDateTime arrivalTime;
    private String chiefComplaint;
    private String emergencyCondition;
    private EmergencyPriority priority;
    private Long assignedDoctorId;
    private String assignedDoctorName;
    private Long departmentId;
    private String departmentName;
    private String bloodPressure;
    private Integer heartRate;
    private Integer oxygenSaturation;
    private Double temperature;
    private Integer respiratoryRate;
    private String treatmentNotes;
    private EmergencyStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
