package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateDoctorRequest;
import com.lifecare.hospital.dto.response.DoctorResponse;
import com.lifecare.hospital.entity.Doctor;

import java.util.List;

public interface DoctorService {
    DoctorResponse createDoctor(CreateDoctorRequest request);
    List<DoctorResponse> getAllDoctors();
    DoctorResponse getDoctorById(Long id);
    List<DoctorResponse> getDoctorsByDepartment(String department);
    DoctorResponse updateDoctor(Long id, CreateDoctorRequest request);
    void deleteDoctor(Long id);
    Doctor findOrCreateDoctorEntity(String firstName, String lastName, String department);
}
