package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.dto.response.DashboardSummaryResponse;
import com.lifecare.hospital.dto.response.EmergencyResponse;
import com.lifecare.hospital.dto.response.WalkInResponse;
import com.lifecare.hospital.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Endpoints for hospital KPIs, today's metrics, and queue monitoring")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get hospital operations summary metrics for admin dashboard")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/walk-in")
    @Operation(summary = "Get walk-in active queue dashboard")
    public ResponseEntity<List<WalkInResponse>> getWalkInDashboard() {
        return ResponseEntity.ok(dashboardService.getWalkInDashboard());
    }

    @GetMapping("/emergency")
    @Operation(summary = "Get active emergency cases dashboard (critical first)")
    public ResponseEntity<List<EmergencyResponse>> getEmergencyDashboard() {
        return ResponseEntity.ok(dashboardService.getEmergencyDashboard());
    }

    @GetMapping("/appointments")
    @Operation(summary = "Get appointments dashboard")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsDashboard() {
        return ResponseEntity.ok(dashboardService.getAppointmentsDashboard());
    }
}
