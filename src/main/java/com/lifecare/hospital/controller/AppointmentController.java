package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateAppointmentRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.entity.AppointmentStatus;
import com.lifecare.hospital.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Endpoints for appointment booking, doctor scheduling, and double-booking prevention")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Book appointment with doctor availability validation and double-booking check")
    public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody CreateAppointmentRequest request,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : request.getEmail();
        return ResponseEntity.ok(appointmentService.createAppointment(request, email));
    }

    @GetMapping
    @Operation(summary = "Get all appointments")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments for a specific patient")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get appointments for a specific doctor")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update appointment details")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id, @RequestBody CreateAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update appointment status (PENDING, CONFIRMED, CHECKED_IN, IN_CONSULTATION, COMPLETED, CANCELLED, NO_SHOW)")
    public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        AppointmentStatus status = AppointmentStatus.valueOf(statusMap.get("status").toUpperCase());
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment")
    public ResponseEntity<ApiResponse<Void>> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment deleted successfully"));
    }
}
