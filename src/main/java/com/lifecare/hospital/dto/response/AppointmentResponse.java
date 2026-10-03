package com.lifecare.hospital.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lifecare.hospital.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;

    // React frontend compatibility aliases
    @JsonProperty("_id")
    public Long getMongoCompatId() {
        return id;
    }

    private Long patientId;
    private Long doctorId;
    private Long departmentId;
    private LocalDate appointmentDate;

    @JsonProperty("appointment_date")
    public String getAppointmentDateStr() {
        return appointmentDate != null ? appointmentDate.toString() : "";
    }

    private LocalTime appointmentTime;
    private String reason;
    private AppointmentStatus status;
    private String notes;

    // Direct fields for existing React dashboard
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String aadhar;
    private String gender;
    private String department;
    private Boolean hasVisited;
    private String address;

    @JsonProperty("doctor")
    public DoctorNested getDoctor() {
        return new DoctorNested(doctorFirstName, doctorLastName);
    }

    private String doctorFirstName;
    private String doctorLastName;

    private LocalDateTime createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DoctorNested {
        private String firstName;
        private String lastName;
    }
}
