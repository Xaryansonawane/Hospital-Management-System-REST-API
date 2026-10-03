package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateDoctorRequest;
import com.lifecare.hospital.dto.response.DoctorResponse;
import com.lifecare.hospital.entity.Doctor;
import com.lifecare.hospital.entity.DoctorStatus;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.DoctorRepository;
import com.lifecare.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .specialization(request.getSpecialization())
                .department(request.getDepartment())
                .phone(request.getPhone())
                .email(request.getEmail())
                .experience(request.getExperience() != null ? request.getExperience() : 5)
                .consultationFee(request.getConsultationFee() != null ? request.getConsultationFee() : new BigDecimal("500.00"))
                .availability(request.getAvailability() != null ? request.getAvailability() : "Mon-Fri 09:00-17:00")
                .status(request.getStatus() != null ? request.getStatus() : DoctorStatus.AVAILABLE)
                .doctorAvatar(request.getDoctorAvatar())
                .build();

        doctor = doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return mapToResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(Long id, CreateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        if (request.getFirstName() != null) doctor.setFirstName(request.getFirstName());
        if (request.getLastName() != null) doctor.setLastName(request.getLastName());
        if (request.getSpecialization() != null) doctor.setSpecialization(request.getSpecialization());
        if (request.getDepartment() != null) doctor.setDepartment(request.getDepartment());
        if (request.getPhone() != null) doctor.setPhone(request.getPhone());
        if (request.getEmail() != null) doctor.setEmail(request.getEmail());
        if (request.getExperience() != null) doctor.setExperience(request.getExperience());
        if (request.getConsultationFee() != null) doctor.setConsultationFee(request.getConsultationFee());
        if (request.getAvailability() != null) doctor.setAvailability(request.getAvailability());
        if (request.getStatus() != null) doctor.setStatus(request.getStatus());
        if (request.getDoctorAvatar() != null) doctor.setDoctorAvatar(request.getDoctorAvatar());

        doctor = doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }

    @Override
    @Transactional
    public Doctor findOrCreateDoctorEntity(String firstName, String lastName, String department) {
        return doctorRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseGet(() -> {
                    Doctor d = Doctor.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .department(department != null ? department : "General")
                            .specialization(department)
                            .status(DoctorStatus.AVAILABLE)
                            .consultationFee(new BigDecimal("500.00"))
                            .experience(5)
                            .build();
                    return doctorRepository.save(d);
                });
    }

    private DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .department(doctor.getDepartment())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .experience(doctor.getExperience())
                .consultationFee(doctor.getConsultationFee())
                .availability(doctor.getAvailability())
                .status(doctor.getStatus())
                .doctorAvatar(doctor.getDoctorAvatar())
                .build();
    }
}
