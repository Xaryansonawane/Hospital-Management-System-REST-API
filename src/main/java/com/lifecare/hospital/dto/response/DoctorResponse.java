package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.DoctorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String specialization;
    private String department;
    private String phone;
    private String email;
    private Integer experience;
    private BigDecimal consultationFee;
    private String availability;
    private DoctorStatus status;
    private String doctorAvatar;
}
