package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreatePatientRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.PatientMedicalHistoryResponse;
import com.lifecare.hospital.dto.response.PatientResponse;
import com.lifecare.hospital.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Endpoints for patient registration, profiles, and central medical record")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Register new patient with auto-generated ID (PAT-2026-00001)")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        return ResponseEntity.ok(patientService.createPatient(request));
    }

    @GetMapping
    @Operation(summary = "Get list of all patients")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient profile")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody CreatePatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient record")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully"));
    }

    @GetMapping("/{id}/medical-history")
    @Operation(summary = "Central patient medical record aggregating all visits, emergencies, consultations, prescriptions, admissions, and bills")
    public ResponseEntity<PatientMedicalHistoryResponse> getMedicalHistory(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getMedicalHistory(id));
    }
}
