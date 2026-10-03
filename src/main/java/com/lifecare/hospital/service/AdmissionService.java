package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateAdmissionRequest;
import com.lifecare.hospital.dto.response.AdmissionResponse;
import com.lifecare.hospital.entity.AdmissionStatus;

import java.util.List;

public interface AdmissionService {
    AdmissionResponse admitPatient(CreateAdmissionRequest request);
    AdmissionResponse getAdmissionById(Long id);
    List<AdmissionResponse> getAllAdmissions();
    List<AdmissionResponse> getAdmissionsByPatientId(Long patientId);
    AdmissionResponse updateStatus(Long id, AdmissionStatus status);
    AdmissionResponse dischargePatient(Long id);
}
