package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateAppointmentRequest;
import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.entity.AppointmentStatus;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse createAppointment(CreateAppointmentRequest request, String currentUserEmail);
    List<AppointmentResponse> getAllAppointments();
    AppointmentResponse getAppointmentById(Long id);
    List<AppointmentResponse> getAppointmentsByPatientId(Long patientId);
    List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId);
    AppointmentResponse updateAppointment(Long id, CreateAppointmentRequest request);
    AppointmentResponse updateStatus(Long id, AppointmentStatus status);
    void deleteAppointment(Long id);
}
