package com.lifecare.hospital.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private Long departmentId;
    private String subject;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @NotBlank(message = "Message is required")
    @Size(min = 5, message = "Message must contain at least 5 characters")
    private String message;
}
