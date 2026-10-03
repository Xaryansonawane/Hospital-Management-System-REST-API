package com.lifecare.hospital.controller;

import com.lifecare.hospital.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Endpoints for hospital management analytics and operational reports")
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/daily-patients")
    @Operation(summary = "Daily patient volume report")
    public ResponseEntity<Map<String, Object>> getDailyPatients() {
        return ResponseEntity.ok(reportsService.getDailyPatientsReport());
    }

    @GetMapping("/walk-ins")
    @Operation(summary = "Walk-in registration and throughput report")
    public ResponseEntity<Map<String, Object>> getWalkInsReport() {
        return ResponseEntity.ok(reportsService.getWalkInsReport());
    }

    @GetMapping("/emergency")
    @Operation(summary = "Emergency arrivals and triage report")
    public ResponseEntity<Map<String, Object>> getEmergencyReport() {
        return ResponseEntity.ok(reportsService.getEmergencyReport());
    }

    @GetMapping("/appointments")
    @Operation(summary = "Appointments scheduling and status report")
    public ResponseEntity<Map<String, Object>> getAppointmentsReport() {
        return ResponseEntity.ok(reportsService.getAppointmentsReport());
    }

    @GetMapping("/doctor-consultations")
    @Operation(summary = "Doctor consultation throughput report")
    public ResponseEntity<Map<String, Object>> getDoctorConsultationsReport() {
        return ResponseEntity.ok(reportsService.getDoctorConsultationsReport());
    }

    @GetMapping("/pharmacy-sales")
    @Operation(summary = "Hospital pharmacy sales and revenue report")
    public ResponseEntity<Map<String, Object>> getPharmacySalesReport() {
        return ResponseEntity.ok(reportsService.getPharmacySalesReport());
    }

    @GetMapping("/revenue")
    @Operation(summary = "Hospital financial revenue report (daily and monthly)")
    public ResponseEntity<Map<String, Object>> getRevenueReport() {
        return ResponseEntity.ok(reportsService.getRevenueReport());
    }

    @GetMapping("/admissions")
    @Operation(summary = "Inpatient admissions and discharges report")
    public ResponseEntity<Map<String, Object>> getAdmissionsReport() {
        return ResponseEntity.ok(reportsService.getAdmissionsReport());
    }

    @GetMapping("/bed-occupancy")
    @Operation(summary = "Ward and bed occupancy rate report")
    public ResponseEntity<Map<String, Object>> getBedOccupancyReport() {
        return ResponseEntity.ok(reportsService.getBedOccupancyReport());
    }
}
