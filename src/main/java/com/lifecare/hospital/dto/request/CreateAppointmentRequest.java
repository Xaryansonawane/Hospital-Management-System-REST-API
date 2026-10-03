package com.lifecare.hospital.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private Long departmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
    private String notes;

    // Backward-compatibility fields with existing React frontend
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String aadhar;
    private String dob;
    private String gender;

    @JsonProperty("appointment_date")
    private String appointmentDateStr;

    private String department;

    @JsonProperty("doctor_firstName")
    private String doctorFirstName;

    @JsonProperty("doctor_lastName")
    private String doctorLastName;

    private Boolean hasVisited;
    private String address;
}
