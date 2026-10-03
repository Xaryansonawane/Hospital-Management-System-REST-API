package com.lifecare.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long patientId;
    private Long departmentId;
    private String departmentName;
    private String subject;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
