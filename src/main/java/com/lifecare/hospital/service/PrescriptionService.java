package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreatePrescriptionRequest;
import com.lifecare.hospital.dto.response.PrescriptionResponse;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse createPrescription(CreatePrescriptionRequest request);
    PrescriptionResponse getPrescriptionById(Long id);
    List<PrescriptionResponse> getPrescriptionsByPatientId(Long patientId);
}
