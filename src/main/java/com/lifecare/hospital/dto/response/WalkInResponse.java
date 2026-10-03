package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.Priority;
import com.lifecare.hospital.entity.WalkInStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkInResponse {
    private Long id;
    private String tokenNumber;
    private Long patientId;
    private String patientName;
    private Long departmentId;
    private String departmentName;
    private Long doctorId;
    private String doctorName;
    private String visitReason;
    private Priority priority;
    private LocalDateTime arrivalTime;
    private LocalDateTime consultationStartTime;
    private LocalDateTime consultationEndTime;
    private WalkInStatus status;
    private LocalDateTime createdAt;
}
