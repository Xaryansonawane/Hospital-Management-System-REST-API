package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateConsultationRequest;
import com.lifecare.hospital.dto.response.ConsultationResponse;

import java.util.List;

public interface ConsultationService {
    ConsultationResponse createConsultation(CreateConsultationRequest request);
    ConsultationResponse getConsultationById(Long id);
    List<ConsultationResponse> getConsultationsByPatientId(Long patientId);
    List<ConsultationResponse> getConsultationsByDoctorId(Long doctorId);
    ConsultationResponse updateConsultation(Long id, CreateConsultationRequest request);
}
