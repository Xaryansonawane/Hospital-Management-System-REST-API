package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateWalkInRequest;
import com.lifecare.hospital.dto.response.WalkInResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.BadRequestException;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.DepartmentRepository;
import com.lifecare.hospital.repository.DoctorRepository;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.repository.WalkInVisitRepository;
import com.lifecare.hospital.service.WalkInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkInServiceImpl implements WalkInService {

    private final WalkInVisitRepository walkInVisitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public synchronized WalkInResponse createWalkIn(CreateWalkInRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        long nextId = walkInVisitRepository.findTopByOrderByIdDesc()
                .map(v -> v.getId() + 1)
                .orElse(1L);

        String token = String.format("W-%03d", nextId);

        // Ensure no active duplicate token exists
        List<WalkInStatus> activeStatuses = Arrays.asList(WalkInStatus.WAITING, WalkInStatus.CALLED, WalkInStatus.IN_CONSULTATION);
        if (walkInVisitRepository.findByTokenNumberAndStatusIn(token, activeStatuses).isPresent()) {
            throw new BadRequestException("Duplicate active token detected: " + token);
        }

        Priority priority = request.getPriority() != null ? request.getPriority() : Priority.MEDIUM;

        WalkInVisit visit = WalkInVisit.builder()
                .tokenNumber(token)
                .patientId(patient.getId())
                .departmentId(request.getDepartmentId())
                .doctorId(request.getDoctorId())
                .visitReason(request.getVisitReason())
                .priority(priority)
                .arrivalTime(LocalDateTime.now())
                .status(WalkInStatus.WAITING)
                .build();

        visit = walkInVisitRepository.save(visit);
        return mapToResponse(visit);
    }

    @Override
    public List<WalkInResponse> getAllWalkIns() {
        return walkInVisitRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<WalkInResponse> getActiveQueue() {
        List<WalkInStatus> queueStatuses = Arrays.asList(WalkInStatus.WAITING, WalkInStatus.CALLED);
        return walkInVisitRepository.findByStatusInOrderByPriorityDescArrivalTimeAsc(queueStatuses).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WalkInResponse getWalkInById(Long id) {
        WalkInVisit visit = walkInVisitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Walk-in visit not found with id: " + id));
        return mapToResponse(visit);
    }

    @Override
    @Transactional
    public WalkInResponse updateStatus(Long id, WalkInStatus status) {
        WalkInVisit visit = walkInVisitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Walk-in visit not found with id: " + id));

        visit.setStatus(status);
        if (status == WalkInStatus.IN_CONSULTATION && visit.getConsultationStartTime() == null) {
            visit.setConsultationStartTime(LocalDateTime.now());
        } else if (status == WalkInStatus.COMPLETED || status == WalkInStatus.CANCELLED) {
            visit.setConsultationEndTime(LocalDateTime.now());
        }

        visit = walkInVisitRepository.save(visit);
        return mapToResponse(visit);
    }

    private WalkInResponse mapToResponse(WalkInVisit visit) {
        String patientName = patientRepository.findById(visit.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");

        String doctorName = visit.getDoctorId() != null
                ? doctorRepository.findById(visit.getDoctorId())
                .map(d -> d.getFirstName() + " " + d.getLastName())
                .orElse("Not Assigned")
                : "Not Assigned";

        String departmentName = visit.getDepartmentId() != null
                ? departmentRepository.findById(visit.getDepartmentId())
                .map(Department::getName)
                .orElse("General")
                : "General";

        return WalkInResponse.builder()
                .id(visit.getId())
                .tokenNumber(visit.getTokenNumber())
                .patientId(visit.getPatientId())
                .patientName(patientName)
                .departmentId(visit.getDepartmentId())
                .departmentName(departmentName)
                .doctorId(visit.getDoctorId())
                .doctorName(doctorName)
                .visitReason(visit.getVisitReason())
                .priority(visit.getPriority())
                .arrivalTime(visit.getArrivalTime())
                .consultationStartTime(visit.getConsultationStartTime())
                .consultationEndTime(visit.getConsultationEndTime())
                .status(visit.getStatus())
                .createdAt(visit.getCreatedAt())
                .build();
    }
}
