package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.AdmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long departmentId;
    private String departmentName;
    private Long wardId;
    private String wardName;
    private Long bedId;
    private String bedNumber;
    private LocalDateTime admissionDate;
    private String reason;
    private String diagnosis;
    private AdmissionStatus status;
    private LocalDateTime dischargeDate;
}
