package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateEmergencyRequest;
import com.lifecare.hospital.dto.response.EmergencyResponse;
import com.lifecare.hospital.entity.EmergencyPriority;
import com.lifecare.hospital.entity.EmergencyStatus;

import java.util.List;

public interface EmergencyService {
    EmergencyResponse createEmergencyCase(CreateEmergencyRequest request);
    List<EmergencyResponse> getAllCases();
    List<EmergencyResponse> getActiveCases();
    EmergencyResponse getCaseById(Long id);
    EmergencyResponse updateCase(Long id, CreateEmergencyRequest request);
    EmergencyResponse updatePriority(Long id, EmergencyPriority priority);
    EmergencyResponse updateStatus(Long id, EmergencyStatus status);
}
