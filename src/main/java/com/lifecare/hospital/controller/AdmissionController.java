package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateAdmissionRequest;
import com.lifecare.hospital.dto.response.AdmissionResponse;
import com.lifecare.hospital.entity.AdmissionStatus;
import com.lifecare.hospital.service.AdmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admissions")
@RequiredArgsConstructor
@Tag(name = "Admissions", description = "Endpoints for inpatient admissions, bed allocation, and discharge workflow")
public class AdmissionController {

    private final AdmissionService admissionService;

    @PostMapping
    @Operation(summary = "Admit patient, validating and locking available bed atomically")
    public ResponseEntity<AdmissionResponse> admitPatient(@Valid @RequestBody CreateAdmissionRequest request) {
        return ResponseEntity.ok(admissionService.admitPatient(request));
    }

    @GetMapping
    @Operation(summary = "Get list of all admissions")
    public ResponseEntity<List<AdmissionResponse>> getAllAdmissions() {
        return ResponseEntity.ok(admissionService.getAllAdmissions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get admission details by ID")
    public ResponseEntity<AdmissionResponse> getAdmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(admissionService.getAdmissionById(id));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all admissions for a specific patient")
    public ResponseEntity<List<AdmissionResponse>> getAdmissionsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(admissionService.getAdmissionsByPatientId(patientId));
    }

    @PutMapping("/{id}/discharge")
    @Operation(summary = "Discharge patient and release bed atomically (OCCUPIED -> AVAILABLE)")
    public ResponseEntity<AdmissionResponse> dischargePatient(@PathVariable Long id) {
        return ResponseEntity.ok(admissionService.dischargePatient(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update admission status (ADMITTED, UNDER_TREATMENT, READY_FOR_DISCHARGE, DISCHARGED)")
    public ResponseEntity<AdmissionResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        AdmissionStatus status = AdmissionStatus.valueOf(statusMap.get("status").toUpperCase());
        return ResponseEntity.ok(admissionService.updateStatus(id, status));
    }
}
