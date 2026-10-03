package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateDoctorRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.DoctorResponse;
import com.lifecare.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Endpoints for doctor management, availability, and specialization profiles")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Register new doctor profile")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(doctorService.createDoctor(request));
    }

    @GetMapping
    @Operation(summary = "Get all registered doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors(@RequestParam(required = false) String department) {
        if (department != null && !department.isBlank()) {
            return ResponseEntity.ok(doctorService.getDoctorsByDepartment(department));
        }
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor profile by ID")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor profile and availability status")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor profile")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully"));
    }
}
