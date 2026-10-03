package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateConsultationRequest;
import com.lifecare.hospital.dto.response.ConsultationResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final WalkInVisitRepository walkInVisitRepository;
    private final EmergencyCaseRepository emergencyCaseRepository;

    @Override
    @Transactional
    public ConsultationResponse createConsultation(CreateConsultationRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        Consultation consultation = Consultation.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .appointmentId(request.getAppointmentId())
                .walkInVisitId(request.getWalkInVisitId())
                .emergencyCaseId(request.getEmergencyCaseId())
                .source(request.getSource())
                .symptoms(request.getSymptoms())
                .diagnosis(request.getDiagnosis())
                .vitals(request.getVitals())
                .notes(request.getNotes())
                .followUpDate(request.getFollowUpDate())
                .consultationDate(LocalDateTime.now())
                .build();

        consultation = consultationRepository.save(consultation);

        // Update originating workflow status
        if (request.getSource() == ConsultationSource.APPOINTMENT && request.getAppointmentId() != null) {
            appointmentRepository.findById(request.getAppointmentId()).ifPresent(a -> {
                a.setStatus(AppointmentStatus.COMPLETED);
                appointmentRepository.save(a);
            });
        } else if (request.getSource() == ConsultationSource.WALK_IN && request.getWalkInVisitId() != null) {
            walkInVisitRepository.findById(request.getWalkInVisitId()).ifPresent(w -> {
                w.setStatus(WalkInStatus.COMPLETED);
                w.setConsultationEndTime(LocalDateTime.now());
                walkInVisitRepository.save(w);
            });
        } else if (request.getSource() == ConsultationSource.EMERGENCY && request.getEmergencyCaseId() != null) {
            emergencyCaseRepository.findById(request.getEmergencyCaseId()).ifPresent(e -> {
                e.setStatus(EmergencyStatus.UNDER_TREATMENT);
                emergencyCaseRepository.save(e);
            });
        }

        return mapToResponse(consultation);
    }

    @Override
    public ConsultationResponse getConsultationById(Long id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with id: " + id));
        return mapToResponse(consultation);
    }

    @Override
    public List<ConsultationResponse> getConsultationsByPatientId(Long patientId) {
        return consultationRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationResponse> getConsultationsByDoctorId(Long doctorId) {
        return consultationRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConsultationResponse updateConsultation(Long id, CreateConsultationRequest request) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with id: " + id));

        if (request.getSymptoms() != null) consultation.setSymptoms(request.getSymptoms());
        if (request.getDiagnosis() != null) consultation.setDiagnosis(request.getDiagnosis());
        if (request.getVitals() != null) consultation.setVitals(request.getVitals());
        if (request.getNotes() != null) consultation.setNotes(request.getNotes());
        if (request.getFollowUpDate() != null) consultation.setFollowUpDate(request.getFollowUpDate());

        consultation = consultationRepository.save(consultation);
        return mapToResponse(consultation);
    }

    private ConsultationResponse mapToResponse(Consultation c) {
        String patientName = patientRepository.findById(c.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");

        String doctorName = doctorRepository.findById(c.getDoctorId())
                .map(d -> d.getFirstName() + " " + d.getLastName())
                .orElse("Unknown");

        return ConsultationResponse.builder()
                .id(c.getId())
                .patientId(c.getPatientId())
                .patientName(patientName)
                .doctorId(c.getDoctorId())
                .doctorName(doctorName)
                .appointmentId(c.getAppointmentId())
                .walkInVisitId(c.getWalkInVisitId())
                .emergencyCaseId(c.getEmergencyCaseId())
                .source(c.getSource())
                .symptoms(c.getSymptoms())
                .diagnosis(c.getDiagnosis())
                .vitals(c.getVitals())
                .notes(c.getNotes())
                .followUpDate(c.getFollowUpDate())
                .consultationDate(c.getConsultationDate())
                .build();
    }
}
