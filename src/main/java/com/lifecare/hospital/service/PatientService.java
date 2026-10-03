package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreatePatientRequest;
import com.lifecare.hospital.dto.response.PatientMedicalHistoryResponse;
import com.lifecare.hospital.dto.response.PatientResponse;

import java.util.List;

public interface PatientService {
    PatientResponse createPatient(CreatePatientRequest request);
    List<PatientResponse> getAllPatients();
    PatientResponse getPatientById(Long id);
    PatientResponse updatePatient(Long id, CreatePatientRequest request);
    void deletePatient(Long id);
    PatientMedicalHistoryResponse getMedicalHistory(Long patientId);
}
