package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.response.BedResponse;
import com.lifecare.hospital.entity.BedStatus;
import com.lifecare.hospital.service.BedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
@Tag(name = "Beds", description = "Endpoints for hospital bed management and real-time bed availability")
public class BedController {

    private final BedService bedService;

    @GetMapping
    @Operation(summary = "Get list of all hospital beds across wards")
    public ResponseEntity<List<BedResponse>> getAllBeds() {
        return ResponseEntity.ok(bedService.getAllBeds());
    }

    @GetMapping("/available")
    @Operation(summary = "Get currently available beds ready for admission")
    public ResponseEntity<List<BedResponse>> getAvailableBeds() {
        return ResponseEntity.ok(bedService.getAvailableBeds());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bed details by ID")
    public ResponseEntity<BedResponse> getBedById(@PathVariable Long id) {
        return ResponseEntity.ok(bedService.getBedById(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update bed status (AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE)")
    public ResponseEntity<BedResponse> updateBedStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        BedStatus status = BedStatus.valueOf(statusMap.get("status").toUpperCase());
        return ResponseEntity.ok(bedService.updateBedStatus(id, status));
    }
}
