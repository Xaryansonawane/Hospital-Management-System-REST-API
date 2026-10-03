package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreatePrescriptionRequest;
import com.lifecare.hospital.dto.response.PrescriptionResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.DoctorRepository;
import com.lifecare.hospital.repository.MedicineRepository;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.repository.PrescriptionRepository;
import com.lifecare.hospital.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicineRepository medicineRepository;

    @Override
    @Transactional
    public PrescriptionResponse createPrescription(CreatePrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        Prescription prescription = Prescription.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .consultationId(request.getConsultationId())
                .notes(request.getNotes())
                .prescriptionDate(LocalDateTime.now())
                .build();

        for (CreatePrescriptionRequest.PrescriptionItemRequest itemReq : request.getItems()) {
            String medName = itemReq.getMedicineName();
            if (medName == null || medName.isBlank()) {
                medName = medicineRepository.findById(itemReq.getMedicineId())
                        .map(Medicine::getMedicineName)
                        .orElse("Medicine #" + itemReq.getMedicineId());
            }

            PrescriptionItem item = PrescriptionItem.builder()
                    .medicineId(itemReq.getMedicineId())
                    .medicineName(medName)
                    .dosage(itemReq.getDosage() != null ? itemReq.getDosage() : "As directed")
                    .frequency(itemReq.getFrequency() != null ? itemReq.getFrequency() : "1-0-1")
                    .duration(itemReq.getDuration() != null ? itemReq.getDuration() : "5 Days")
                    .instructions(itemReq.getInstructions() != null ? itemReq.getInstructions() : "After food")
                    .build();

            prescription.addItem(item);
        }

        prescription = prescriptionRepository.save(prescription);
        return mapToResponse(prescription);
    }

    @Override
    public PrescriptionResponse getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        return mapToResponse(prescription);
    }

    @Override
    public List<PrescriptionResponse> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PrescriptionResponse mapToResponse(Prescription p) {
        String patientName = patientRepository.findById(p.getPatientId())
                .map(pat -> pat.getFirstName() + " " + pat.getLastName())
                .orElse("Unknown");

        String doctorName = doctorRepository.findById(p.getDoctorId())
                .map(d -> d.getFirstName() + " " + d.getLastName())
                .orElse("Unknown");

        List<PrescriptionResponse.PrescriptionItemResponse> itemResponses = p.getItems().stream()
                .map(i -> PrescriptionResponse.PrescriptionItemResponse.builder()
                        .id(i.getId())
                        .medicineId(i.getMedicineId())
                        .medicineName(i.getMedicineName())
                        .dosage(i.getDosage())
                        .frequency(i.getFrequency())
                        .duration(i.getDuration())
                        .instructions(i.getInstructions())
                        .build())
                .collect(Collectors.toList());

        return PrescriptionResponse.builder()
                .id(p.getId())
                .patientId(p.getPatientId())
                .patientName(patientName)
                .doctorId(p.getDoctorId())
                .doctorName(doctorName)
                .consultationId(p.getConsultationId())
                .prescriptionDate(p.getPrescriptionDate())
                .notes(p.getNotes())
                .items(itemResponses)
                .build();
    }
}
