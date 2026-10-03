package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateEmergencyRequest;
import com.lifecare.hospital.dto.response.EmergencyResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.DepartmentRepository;
import com.lifecare.hospital.repository.DoctorRepository;
import com.lifecare.hospital.repository.EmergencyCaseRepository;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.service.EmergencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyServiceImpl implements EmergencyService {

    private final EmergencyCaseRepository emergencyCaseRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public EmergencyResponse createEmergencyCase(CreateEmergencyRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        EmergencyPriority priority = request.getPriority() != null ? request.getPriority() : EmergencyPriority.HIGH;
        EmergencyStatus status = request.getStatus() != null ? request.getStatus() : EmergencyStatus.REGISTERED;

        EmergencyCase emergencyCase = EmergencyCase.builder()
                .patientId(patient.getId())
                .arrivalTime(LocalDateTime.now())
                .chiefComplaint(request.getChiefComplaint())
                .emergencyCondition(request.getEmergencyCondition())
                .priority(priority)
                .assignedDoctorId(request.getAssignedDoctorId())
                .departmentId(request.getDepartmentId())
                .bloodPressure(request.getBloodPressure())
                .heartRate(request.getHeartRate())
                .oxygenSaturation(request.getOxygenSaturation())
                .temperature(request.getTemperature())
                .respiratoryRate(request.getRespiratoryRate())
                .treatmentNotes(request.getTreatmentNotes())
                .status(status)
                .build();

        emergencyCase = emergencyCaseRepository.save(emergencyCase);
        return mapToResponse(emergencyCase);
    }

    @Override
    public List<EmergencyResponse> getAllCases() {
        return emergencyCaseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyResponse> getActiveCases() {
        List<EmergencyStatus> activeStatuses = Arrays.asList(
                EmergencyStatus.REGISTERED,
                EmergencyStatus.TRIAGE,
                EmergencyStatus.UNDER_TREATMENT
        );
        return emergencyCaseRepository.findActiveCasesOrdered(activeStatuses).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmergencyResponse getCaseById(Long id) {
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency case not found with id: " + id));
        return mapToResponse(emergencyCase);
    }

    @Override
    @Transactional
    public EmergencyResponse updateCase(Long id, CreateEmergencyRequest request) {
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency case not found with id: " + id));

        if (request.getChiefComplaint() != null) emergencyCase.setChiefComplaint(request.getChiefComplaint());
        if (request.getEmergencyCondition() != null) emergencyCase.setEmergencyCondition(request.getEmergencyCondition());
        if (request.getPriority() != null) emergencyCase.setPriority(request.getPriority());
        if (request.getAssignedDoctorId() != null) emergencyCase.setAssignedDoctorId(request.getAssignedDoctorId());
        if (request.getDepartmentId() != null) emergencyCase.setDepartmentId(request.getDepartmentId());
        if (request.getBloodPressure() != null) emergencyCase.setBloodPressure(request.getBloodPressure());
        if (request.getHeartRate() != null) emergencyCase.setHeartRate(request.getHeartRate());
        if (request.getOxygenSaturation() != null) emergencyCase.setOxygenSaturation(request.getOxygenSaturation());
        if (request.getTemperature() != null) emergencyCase.setTemperature(request.getTemperature());
        if (request.getRespiratoryRate() != null) emergencyCase.setRespiratoryRate(request.getRespiratoryRate());
        if (request.getTreatmentNotes() != null) emergencyCase.setTreatmentNotes(request.getTreatmentNotes());
        if (request.getStatus() != null) emergencyCase.setStatus(request.getStatus());

        emergencyCase = emergencyCaseRepository.save(emergencyCase);
        return mapToResponse(emergencyCase);
    }

    @Override
    @Transactional
    public EmergencyResponse updatePriority(Long id, EmergencyPriority priority) {
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency case not found with id: " + id));

        emergencyCase.setPriority(priority);
        emergencyCase = emergencyCaseRepository.save(emergencyCase);
        return mapToResponse(emergencyCase);
    }

    @Override
    @Transactional
    public EmergencyResponse updateStatus(Long id, EmergencyStatus status) {
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency case not found with id: " + id));

        emergencyCase.setStatus(status);
        emergencyCase = emergencyCaseRepository.save(emergencyCase);
        return mapToResponse(emergencyCase);
    }

    private EmergencyResponse mapToResponse(EmergencyCase emergencyCase) {
        String patientName = patientRepository.findById(emergencyCase.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");

        String doctorName = emergencyCase.getAssignedDoctorId() != null
                ? doctorRepository.findById(emergencyCase.getAssignedDoctorId())
                .map(d -> d.getFirstName() + " " + d.getLastName())
                .orElse("Not Assigned")
                : "Not Assigned";

        String departmentName = emergencyCase.getDepartmentId() != null
                ? departmentRepository.findById(emergencyCase.getDepartmentId())
                .map(Department::getName)
                .orElse("Emergency")
                : "Emergency";

        return EmergencyResponse.builder()
                .id(emergencyCase.getId())
                .patientId(emergencyCase.getPatientId())
                .patientName(patientName)
                .arrivalTime(emergencyCase.getArrivalTime())
                .chiefComplaint(emergencyCase.getChiefComplaint())
                .emergencyCondition(emergencyCase.getEmergencyCondition())
                .priority(emergencyCase.getPriority())
                .assignedDoctorId(emergencyCase.getAssignedDoctorId())
                .assignedDoctorName(doctorName)
                .departmentId(emergencyCase.getDepartmentId())
                .departmentName(departmentName)
                .bloodPressure(emergencyCase.getBloodPressure())
                .heartRate(emergencyCase.getHeartRate())
                .oxygenSaturation(emergencyCase.getOxygenSaturation())
                .temperature(emergencyCase.getTemperature())
                .respiratoryRate(emergencyCase.getRespiratoryRate())
                .treatmentNotes(emergencyCase.getTreatmentNotes())
                .status(emergencyCase.getStatus())
                .createdAt(emergencyCase.getCreatedAt())
                .updatedAt(emergencyCase.getUpdatedAt())
                .build();
    }
}
