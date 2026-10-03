package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateAdmissionRequest;
import com.lifecare.hospital.dto.response.AdmissionResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.BedUnavailableException;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.AdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final BedRepository bedRepository;
    private final WardRepository wardRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public AdmissionResponse admitPatient(CreateAdmissionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found with id: " + request.getBedId()));

        // ATOMIC BED AVAILABILITY VALIDATION
        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new BedUnavailableException("Cannot admit patient: Bed " + bed.getBedNumber() + " is already occupied or unavailable!");
        }

        // ATOMIC BED OCCUPATION
        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);

        Admission admission = Admission.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .departmentId(request.getDepartmentId())
                .wardId(request.getWardId())
                .bedId(bed.getId())
                .admissionDate(LocalDateTime.now())
                .reason(request.getReason())
                .diagnosis(request.getDiagnosis())
                .status(AdmissionStatus.ADMITTED)
                .build();

        admission = admissionRepository.save(admission);
        return mapToResponse(admission);
    }

    @Override
    public AdmissionResponse getAdmissionById(Long id) {
        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id: " + id));
        return mapToResponse(admission);
    }

    @Override
    public List<AdmissionResponse> getAllAdmissions() {
        return admissionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdmissionResponse> getAdmissionsByPatientId(Long patientId) {
        return admissionRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdmissionResponse updateStatus(Long id, AdmissionStatus status) {
        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id: " + id));

        if (status == AdmissionStatus.DISCHARGED) {
            return dischargePatient(id);
        }

        admission.setStatus(status);
        admission = admissionRepository.save(admission);
        return mapToResponse(admission);
    }

    @Override
    @Transactional
    public AdmissionResponse dischargePatient(Long id) {
        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id: " + id));

        admission.setStatus(AdmissionStatus.DISCHARGED);
        admission.setDischargeDate(LocalDateTime.now());
        admission = admissionRepository.save(admission);

        // ATOMIC BED RELEASE
        bedRepository.findById(admission.getBedId()).ifPresent(bed -> {
            bed.setStatus(BedStatus.AVAILABLE);
            bedRepository.save(bed);
        });

        return mapToResponse(admission);
    }

    private AdmissionResponse mapToResponse(Admission adm) {
        String patientName = patientRepository.findById(adm.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");

        String doctorName = doctorRepository.findById(adm.getDoctorId())
                .map(d -> d.getFirstName() + " " + d.getLastName())
                .orElse("Unknown");

        String wardName = wardRepository.findById(adm.getWardId())
                .map(Ward::getName)
                .orElse("General");

        String bedNumber = bedRepository.findById(adm.getBedId())
                .map(Bed::getBedNumber)
                .orElse("B-Unknown");

        String departmentName = adm.getDepartmentId() != null
                ? departmentRepository.findById(adm.getDepartmentId()).map(Department::getName).orElse("General")
                : "General";

        return AdmissionResponse.builder()
                .id(adm.getId())
                .patientId(adm.getPatientId())
                .patientName(patientName)
                .doctorId(adm.getDoctorId())
                .doctorName(doctorName)
                .departmentId(adm.getDepartmentId())
                .departmentName(departmentName)
                .wardId(adm.getWardId())
                .wardName(wardName)
                .bedId(adm.getBedId())
                .bedNumber(bedNumber)
                .admissionDate(adm.getAdmissionDate())
                .reason(adm.getReason())
                .diagnosis(adm.getDiagnosis())
                .status(adm.getStatus())
                .dischargeDate(adm.getDischargeDate())
                .build();
    }
}
