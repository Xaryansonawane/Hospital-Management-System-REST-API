package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateMedicineRequest;
import com.lifecare.hospital.dto.request.CreatePharmacySaleRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.MedicineResponse;
import com.lifecare.hospital.dto.response.PharmacySaleResponse;
import com.lifecare.hospital.entity.PaymentMethod;
import com.lifecare.hospital.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
@Tag(name = "Pharmacy", description = "Endpoints for hospital pharmacy, medicines, stock tracking, and prescription dispensing")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping("/medicines")
    @Operation(summary = "Add new medicine to pharmacy inventory")
    public ResponseEntity<MedicineResponse> createMedicine(@Valid @RequestBody CreateMedicineRequest request) {
        return ResponseEntity.ok(pharmacyService.createMedicine(request));
    }

    @GetMapping("/medicines")
    @Operation(summary = "Get list of all medicines in pharmacy")
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        return ResponseEntity.ok(pharmacyService.getAllMedicines());
    }

    @GetMapping("/medicines/{id}")
    @Operation(summary = "Get medicine details by ID")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(pharmacyService.getMedicineById(id));
    }

    @PutMapping("/medicines/{id}")
    @Operation(summary = "Update medicine details and stock")
    public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Long id, @RequestBody CreateMedicineRequest request) {
        return ResponseEntity.ok(pharmacyService.updateMedicine(id, request));
    }

    @DeleteMapping("/medicines/{id}")
    @Operation(summary = "Delete medicine from catalog")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Long id) {
        pharmacyService.deleteMedicine(id);
        return ResponseEntity.ok(ApiResponse.success("Medicine deleted successfully"));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Detect low-stock medicines needing replenishment")
    public ResponseEntity<List<MedicineResponse>> getLowStockMedicines() {
        return ResponseEntity.ok(pharmacyService.getLowStockMedicines());
    }

    @GetMapping("/expired")
    @Operation(summary = "Detect expired medicines")
    public ResponseEntity<List<MedicineResponse>> getExpiredMedicines() {
        return ResponseEntity.ok(pharmacyService.getExpiredMedicines());
    }

    @PostMapping("/sales")
    @Operation(summary = "Process pharmacy sale, validate expiry & stock, and decrement inventory atomically")
    public ResponseEntity<PharmacySaleResponse> processSale(@Valid @RequestBody CreatePharmacySaleRequest request) {
        return ResponseEntity.ok(pharmacyService.processSale(request));
    }

    @GetMapping("/sales")
    @Operation(summary = "Get all pharmacy sales records")
    public ResponseEntity<List<PharmacySaleResponse>> getAllSales() {
        return ResponseEntity.ok(pharmacyService.getAllSales());
    }

    @PostMapping("/dispense/{prescriptionId}")
    @Operation(summary = "Dispense doctor's prescription directly through the pharmacy")
    public ResponseEntity<PharmacySaleResponse> dispensePrescription(@PathVariable Long prescriptionId,
                                                                     @RequestBody(required = false) Map<String, String> body) {
        PaymentMethod method = (body != null && body.containsKey("paymentMethod"))
                ? PaymentMethod.valueOf(body.get("paymentMethod").toUpperCase())
                : PaymentMethod.CASH;
        return ResponseEntity.ok(pharmacyService.dispensePrescription(prescriptionId, method));
    }
}
