package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateInvoiceRequest;
import com.lifecare.hospital.dto.request.ProcessPaymentRequest;
import com.lifecare.hospital.dto.response.InvoiceResponse;

import java.util.List;

public interface BillingService {
    InvoiceResponse createInvoice(CreateInvoiceRequest request);
    InvoiceResponse getInvoiceById(Long id);
    InvoiceResponse getInvoiceByNumber(String invoiceNumber);
    List<InvoiceResponse> getInvoicesByPatientId(Long patientId);
    List<InvoiceResponse> getAllInvoices();
    InvoiceResponse processPayment(Long invoiceId, ProcessPaymentRequest request);
}
