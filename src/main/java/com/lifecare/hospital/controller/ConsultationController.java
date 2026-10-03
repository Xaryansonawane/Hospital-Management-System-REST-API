package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateConsultationRequest;
import com.lifecare.hospital.dto.response.ConsultationResponse;
import com.lifecare.hospital.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@Tag(name = "Consultation", description = "Endpoints for doctor consultations originating from Walk-In, Emergency, or Appointments")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping
    @Operation(summary = "Record new consultation and diagnosis, updating originating workflow status")
    public ResponseEntity<ConsultationResponse> createConsultation(@Valid @RequestBody CreateConsultationRequest request) {
        return ResponseEntity.ok(consultationService.createConsultation(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get consultation by ID")
    public ResponseEntity<ConsultationResponse> getConsultationById(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all consultations for a patient")
    public ResponseEntity<List<ConsultationResponse>> getConsultationsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(consultationService.getConsultationsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get all consultations conducted by a doctor")
    public ResponseEntity<List<ConsultationResponse>> getConsultationsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(consultationService.getConsultationsByDoctorId(doctorId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update consultation diagnosis, vitals, or follow-up date")
    public ResponseEntity<ConsultationResponse> updateConsultation(@PathVariable Long id, @RequestBody CreateConsultationRequest request) {
        return ResponseEntity.ok(consultationService.updateConsultation(id, request));
    }
}
