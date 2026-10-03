package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.CreateInvoiceRequest;
import com.lifecare.hospital.dto.request.ProcessPaymentRequest;
import com.lifecare.hospital.dto.response.InvoiceResponse;
import com.lifecare.hospital.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Tag(name = "Billing", description = "Endpoints for invoices, charges, and payment processing")
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices")
    @Operation(summary = "Generate new invoice with server-calculated line item totals using BigDecimal")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return ResponseEntity.ok(billingService.createInvoice(request));
    }

    @GetMapping("/invoices")
    @Operation(summary = "Get list of all hospital invoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(billingService.getAllInvoices());
    }

    @GetMapping("/invoices/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getInvoiceById(id));
    }

    @GetMapping("/invoices/patient/{patientId}")
    @Operation(summary = "Get all invoices for a specific patient")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(billingService.getInvoicesByPatientId(patientId));
    }

    @PostMapping("/invoices/{id}/pay")
    @Operation(summary = "Process invoice payment (CASH, CARD, UPI, INSURANCE)")
    public ResponseEntity<InvoiceResponse> processPayment(@PathVariable Long id, @Valid @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.ok(billingService.processPayment(id, request));
    }
}
