package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateAppointmentRequest;
import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.BadRequestException;
import com.lifecare.hospital.exception.DoubleBookingException;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.AppointmentRepository;
import com.lifecare.hospital.repository.DepartmentRepository;
import com.lifecare.hospital.repository.DoctorRepository;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.repository.UserRepository;
import com.lifecare.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request, String currentUserEmail) {
        // Resolve Doctor
        Doctor doctor = null;
        if (request.getDoctorId() != null) {
            doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
        } else if (request.getDoctorFirstName() != null && request.getDoctorLastName() != null) {
            doctor = doctorRepository.findByFirstNameAndLastName(request.getDoctorFirstName(), request.getDoctorLastName())
                    .orElseGet(() -> {
                        // Fallback check in users if registered as doctor
                        return userRepository.findByFirstNameAndLastNameAndRole(
                                request.getDoctorFirstName(), request.getDoctorLastName(), Role.DOCTOR
                        ).map(u -> doctorRepository.findByUserId(u.getId()).orElse(null))
                         .orElse(null);
                    });
            if (doctor == null) {
                throw new ResourceNotFoundException("Doctor not found: " + request.getDoctorFirstName() + " " + request.getDoctorLastName());
            }
        } else {
            throw new BadRequestException("Doctor information is required!");
        }

        // Validate doctor status
        if (doctor.getStatus() == DoctorStatus.ON_LEAVE) {
            throw new BadRequestException("Doctor is currently on leave!");
        }

        // Resolve Patient
        Patient patient = null;
        if (request.getPatientId() != null) {
            patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
        } else if (currentUserEmail != null) {
            patient = patientRepository.findByEmail(currentUserEmail).orElse(null);
        }

        if (patient == null && request.getEmail() != null) {
            patient = patientRepository.findByEmail(request.getEmail()).orElse(null);
        }

        if (patient == null) {
            // Auto-register patient if necessary
            long count = patientRepository.count() + 1;
            String generatedId = String.format("PAT-2026-%05d", count);
            patient = Patient.builder()
                    .patientId(generatedId)
                    .firstName(request.getFirstName() != null ? request.getFirstName() : "Patient")
                    .lastName(request.getLastName() != null ? request.getLastName() : String.valueOf(count))
                    .email(request.getEmail() != null ? request.getEmail() : currentUserEmail)
                    .phone(request.getPhone() != null ? request.getPhone() : "0000000000")
                    .gender(request.getGender() != null ? request.getGender() : "Others")
                    .address(request.getAddress())
                    .build();
            patient = patientRepository.save(patient);
        }

        // Parse Date & Time
        LocalDate date = request.getAppointmentDate();
        if (date == null && request.getAppointmentDateStr() != null) {
            date = LocalDate.parse(request.getAppointmentDateStr().substring(0, 10));
        }
        if (date == null) {
            date = LocalDate.now().plusDays(1);
        }

        LocalTime time = request.getAppointmentTime();
        if (time == null) {
            time = LocalTime.of(10, 0); // Default 10:00 AM slot
        }

        // DOUBLE-BOOKING PREVENTION
        if (appointmentRepository.existsConflictingAppointment(doctor.getId(), date, time)) {
            throw new DoubleBookingException(
                    String.format("Doctor %s %s is already booked for %s at %s. Please choose a different time slot.",
                            doctor.getFirstName(), doctor.getLastName(), date, time)
            );
        }

        Appointment appointment = Appointment.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .departmentId(request.getDepartmentId())
                .appointmentDate(date)
                .appointmentTime(time)
                .reason(request.getReason() != null ? request.getReason() : "General Consultation")
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                // Legacy fields for React frontend
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .aadhar(patient.getAadhar())
                .dob(patient.getDateOfBirth())
                .gender(patient.getGender())
                .department(doctor.getDepartment())
                .doctorFirstName(doctor.getFirstName())
                .doctorLastName(doctor.getLastName())
                .address(request.getAddress())
                .hasVisited(request.getHasVisited() != null ? request.getHasVisited() : false)
                .build();

        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return mapToResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponse updateAppointment(Long id, CreateAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (request.getAppointmentDate() != null) appointment.setAppointmentDate(request.getAppointmentDate());
        if (request.getAppointmentTime() != null) appointment.setAppointmentTime(request.getAppointmentTime());
        if (request.getReason() != null) appointment.setReason(request.getReason());
        if (request.getNotes() != null) appointment.setNotes(request.getNotes());

        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    @Override
    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(status);
        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponse mapToResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patientId(a.getPatientId())
                .doctorId(a.getDoctorId())
                .departmentId(a.getDepartmentId())
                .appointmentDate(a.getAppointmentDate())
                .appointmentTime(a.getAppointmentTime())
                .reason(a.getReason())
                .status(a.getStatus())
                .notes(a.getNotes())
                .firstName(a.getFirstName())
                .lastName(a.getLastName())
                .email(a.getEmail())
                .phone(a.getPhone())
                .aadhar(a.getAadhar())
                .gender(a.getGender())
                .department(a.getDepartment())
                .doctorFirstName(a.getDoctorFirstName())
                .doctorLastName(a.getDoctorLastName())
                .address(a.getAddress())
                .hasVisited(a.getHasVisited())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
