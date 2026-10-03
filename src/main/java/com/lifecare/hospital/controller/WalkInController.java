package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateWalkInRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.WalkInResponse;
import com.lifecare.hospital.entity.WalkInStatus;
import com.lifecare.hospital.service.WalkInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/walk-ins")
@RequiredArgsConstructor
@Tag(name = "Walk-In", description = "Endpoints for managing walk-in patient flow, queues, and token generation")
public class WalkInController {

    private final WalkInService walkInService;

    @PostMapping
    @Operation(summary = "Register walk-in patient and generate unique token")
    public ResponseEntity<WalkInResponse> createWalkIn(@Valid @RequestBody CreateWalkInRequest request) {
        return ResponseEntity.ok(walkInService.createWalkIn(request));
    }

    @GetMapping
    @Operation(summary = "Get all walk-in visits")
    public ResponseEntity<List<WalkInResponse>> getAllWalkIns() {
        return ResponseEntity.ok(walkInService.getAllWalkIns());
    }

    @GetMapping("/queue")
    @Operation(summary = "Get active walk-in waiting queue ordered by priority and arrival time")
    public ResponseEntity<List<WalkInResponse>> getQueue() {
        return ResponseEntity.ok(walkInService.getActiveQueue());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get walk-in visit by ID")
    public ResponseEntity<WalkInResponse> getWalkInById(@PathVariable Long id) {
        return ResponseEntity.ok(walkInService.getWalkInById(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update walk-in status (WAITING, CALLED, IN_CONSULTATION, COMPLETED, CANCELLED)")
    public ResponseEntity<WalkInResponse> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        WalkInStatus status = WalkInStatus.valueOf(statusMap.get("status").toUpperCase());
        return ResponseEntity.ok(walkInService.updateStatus(id, status));
    }
}
