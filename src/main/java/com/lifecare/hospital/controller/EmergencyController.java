package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateEmergencyRequest;
import com.lifecare.hospital.dto.response.EmergencyResponse;
import com.lifecare.hospital.entity.EmergencyPriority;
import com.lifecare.hospital.entity.EmergencyStatus;
import com.lifecare.hospital.service.EmergencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency")
@RequiredArgsConstructor
@Tag(name = "Emergency", description = "Endpoints for emergency admissions, triage, and critical-first queue")
public class EmergencyController {

    private final EmergencyService emergencyService;

    @PostMapping
    @Operation(summary = "Register emergency case with initial vitals and triage priority")
    public ResponseEntity<EmergencyResponse> createEmergency(@Valid @RequestBody CreateEmergencyRequest request) {
        return ResponseEntity.ok(emergencyService.createEmergencyCase(request));
    }

    @GetMapping
    @Operation(summary = "Get all emergency cases")
    public ResponseEntity<List<EmergencyResponse>> getAllCases() {
        return ResponseEntity.ok(emergencyService.getAllCases());
    }

    @GetMapping("/active")
    @Operation(summary = "Get active emergency cases sorted by critical priority first")
    public ResponseEntity<List<EmergencyResponse>> getActiveCases() {
        return ResponseEntity.ok(emergencyService.getActiveCases());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get emergency case by ID")
    public ResponseEntity<EmergencyResponse> getCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(emergencyService.getCaseById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update emergency case details, doctor assignment, and treatment notes")
    public ResponseEntity<EmergencyResponse> updateCase(@PathVariable Long id, @RequestBody CreateEmergencyRequest request) {
        return ResponseEntity.ok(emergencyService.updateCase(id, request));
    }

    @PutMapping("/{id}/priority")
    @Operation(summary = "Update emergency triage priority (CRITICAL, HIGH, MEDIUM, LOW)")
    public ResponseEntity<EmergencyResponse> updatePriority(@PathVariable Long id, @RequestBody Map<String, String> priorityMap) {
        EmergencyPriority priority = EmergencyPriority.valueOf(priorityMap.get("priority").toUpperCase());
        return ResponseEntity.ok(emergencyService.updatePriority(id, priority));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update emergency status (REGISTERED, TRIAGE, UNDER_TREATMENT, ADMITTED, DISCHARGED, REFERRED)")
    public ResponseEntity<EmergencyResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        EmergencyStatus status = EmergencyStatus.valueOf(statusMap.get("status").toUpperCase());
        return ResponseEntity.ok(emergencyService.updateStatus(id, status));
    }
}
