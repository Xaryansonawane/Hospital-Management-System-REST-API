package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.ConsultationSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long appointmentId;
    private Long walkInVisitId;
    private Long emergencyCaseId;
    private ConsultationSource source;
    private String symptoms;
    private String diagnosis;
    private String vitals;
    private String notes;
    private LocalDate followUpDate;
    private LocalDateTime consultationDate;
}
