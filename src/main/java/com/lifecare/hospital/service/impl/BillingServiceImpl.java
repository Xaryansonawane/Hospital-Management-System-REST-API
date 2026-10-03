package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateInvoiceRequest;
import com.lifecare.hospital.dto.request.ProcessPaymentRequest;
import com.lifecare.hospital.dto.response.InvoiceResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.BadRequestException;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.InvoiceRepository;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.repository.PaymentRepository;
import com.lifecare.hospital.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        long nextId = invoiceRepository.findTopByOrderByIdDesc()
                .map(i -> i.getId() + 1)
                .orElse(1L);

        String invoiceNumber = String.format("INV-2026-%05d", nextId);

        Invoice invoice = Invoice.builder()
                .patientId(patient.getId())
                .invoiceNumber(invoiceNumber)
                .invoiceDate(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .paidAmount(BigDecimal.ZERO)
                .balanceAmount(BigDecimal.ZERO)
                .status(InvoiceStatus.UNPAID)
                .build();

        BigDecimal computedTotal = BigDecimal.ZERO;

        for (CreateInvoiceRequest.InvoiceItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }
            if (itemReq.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Unit price cannot be negative");
            }

            BigDecimal lineTotal = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            computedTotal = computedTotal.add(lineTotal);

            InvoiceItem item = InvoiceItem.builder()
                    .serviceType(itemReq.getServiceType())
                    .description(itemReq.getDescription())
                    .quantity(itemReq.getQuantity())
                    .unitPrice(itemReq.getUnitPrice())
                    .totalPrice(lineTotal)
                    .build();

            invoice.addItem(item);
        }

        invoice.setTotalAmount(computedTotal);
        invoice.setBalanceAmount(computedTotal);

        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return mapToResponse(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with number: " + invoiceNumber));
        return mapToResponse(invoice);
    }

    @Override
    public List<InvoiceResponse> getInvoicesByPatientId(Long patientId) {
        return invoiceRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceResponse processPayment(Long invoiceId, ProcessPaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice is already fully paid!");
        }

        Payment payment = Payment.builder()
                .invoiceId(invoice.getId())
                .paymentDate(LocalDateTime.now())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionReference(request.getTransactionReference())
                .build();
        paymentRepository.save(payment);

        BigDecimal newPaidAmount = invoice.getPaidAmount().add(request.getAmount());
        BigDecimal newBalance = invoice.getTotalAmount().subtract(newPaidAmount);

        invoice.setPaidAmount(newPaidAmount);
        invoice.setBalanceAmount(newBalance.max(BigDecimal.ZERO));

        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIAL);
        }

        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    private InvoiceResponse mapToResponse(Invoice inv) {
        String patientName = patientRepository.findById(inv.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");

        List<InvoiceResponse.InvoiceItemResponse> itemResponses = inv.getItems().stream()
                .map(i -> InvoiceResponse.InvoiceItemResponse.builder()
                        .id(i.getId())
                        .serviceType(i.getServiceType())
                        .description(i.getDescription())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .totalPrice(i.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return InvoiceResponse.builder()
                .id(inv.getId())
                .patientId(inv.getPatientId())
                .patientName(patientName)
                .invoiceNumber(inv.getInvoiceNumber())
                .invoiceDate(inv.getInvoiceDate())
                .totalAmount(inv.getTotalAmount())
                .paidAmount(inv.getPaidAmount())
                .balanceAmount(inv.getBalanceAmount())
                .status(inv.getStatus())
                .items(itemResponses)
                .build();
    }
}
