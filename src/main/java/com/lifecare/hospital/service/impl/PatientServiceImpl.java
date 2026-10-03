package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreatePatientRequest;
import com.lifecare.hospital.dto.response.*;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final WalkInVisitRepository walkInVisitRepository;
    private final EmergencyCaseRepository emergencyCaseRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AdmissionRepository admissionRepository;
    private final InvoiceRepository invoiceRepository;
    private final PharmacySaleRepository pharmacySaleRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;

    @Override
    @Transactional
    public PatientResponse createPatient(CreatePatientRequest request) {
        long count = patientRepository.count() + 1;
        String generatedPatientId = String.format("PAT-2026-%05d", count);

        Patient patient = Patient.builder()
                .patientId(generatedPatientId)
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .aadhar(request.getAadhar())
                .email(request.getEmail())
                .address(request.getAddress())
                .bloodGroup(request.getBloodGroup())
                .emergencyContact(request.getEmergencyContact())
                .allergies(request.getAllergies())
                .medicalHistory(request.getMedicalHistory())
                .build();

        patient = patientRepository.save(patient);
        return mapToResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, CreatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (request.getFirstName() != null) patient.setFirstName(request.getFirstName());
        if (request.getLastName() != null) patient.setLastName(request.getLastName());
        if (request.getDateOfBirth() != null) patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) patient.setGender(request.getGender());
        if (request.getPhone() != null) patient.setPhone(request.getPhone());
        if (request.getAadhar() != null) patient.setAadhar(request.getAadhar());
        if (request.getEmail() != null) patient.setEmail(request.getEmail());
        if (request.getAddress() != null) patient.setAddress(request.getAddress());
        if (request.getBloodGroup() != null) patient.setBloodGroup(request.getBloodGroup());
        if (request.getEmergencyContact() != null) patient.setEmergencyContact(request.getEmergencyContact());
        if (request.getAllergies() != null) patient.setAllergies(request.getAllergies());
        if (request.getMedicalHistory() != null) patient.setMedicalHistory(request.getMedicalHistory());

        patient = patientRepository.save(patient);
        return mapToResponse(patient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    @Override
    public PatientMedicalHistoryResponse getMedicalHistory(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        PatientResponse profile = mapToResponse(patient);

        List<WalkInResponse> walkIns = walkInVisitRepository.findByPatientId(patientId).stream()
                .map(w -> WalkInResponse.builder()
                        .id(w.getId())
                        .tokenNumber(w.getTokenNumber())
                        .patientId(w.getPatientId())
                        .patientName(profile.getFirstName() + " " + profile.getLastName())
                        .departmentId(w.getDepartmentId())
                        .doctorId(w.getDoctorId())
                        .visitReason(w.getVisitReason())
                        .priority(w.getPriority())
                        .arrivalTime(w.getArrivalTime())
                        .status(w.getStatus())
                        .createdAt(w.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        List<EmergencyResponse> emergencies = emergencyCaseRepository.findByPatientId(patientId).stream()
                .map(e -> EmergencyResponse.builder()
                        .id(e.getId())
                        .patientId(e.getPatientId())
                        .patientName(profile.getFirstName() + " " + profile.getLastName())
                        .arrivalTime(e.getArrivalTime())
                        .chiefComplaint(e.getChiefComplaint())
                        .emergencyCondition(e.getEmergencyCondition())
                        .priority(e.getPriority())
                        .bloodPressure(e.getBloodPressure())
                        .heartRate(e.getHeartRate())
                        .treatmentNotes(e.getTreatmentNotes())
                        .status(e.getStatus())
                        .createdAt(e.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        List<AppointmentResponse> appointments = appointmentRepository.findByPatientId(patientId).stream()
                .map(a -> AppointmentResponse.builder()
                        .id(a.getId())
                        .patientId(a.getPatientId())
                        .doctorId(a.getDoctorId())
                        .departmentId(a.getDepartmentId())
                        .appointmentDate(a.getAppointmentDate())
                        .appointmentTime(a.getAppointmentTime())
                        .reason(a.getReason())
                        .status(a.getStatus())
                        .firstName(a.getFirstName())
                        .lastName(a.getLastName())
                        .department(a.getDepartment())
                        .doctorFirstName(a.getDoctorFirstName())
                        .doctorLastName(a.getDoctorLastName())
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        List<ConsultationResponse> consultations = consultationRepository.findByPatientId(patientId).stream()
                .map(c -> ConsultationResponse.builder()
                        .id(c.getId())
                        .patientId(c.getPatientId())
                        .doctorId(c.getDoctorId())
                        .source(c.getSource())
                        .symptoms(c.getSymptoms())
                        .diagnosis(c.getDiagnosis())
                        .vitals(c.getVitals())
                        .notes(c.getNotes())
                        .followUpDate(c.getFollowUpDate())
                        .consultationDate(c.getConsultationDate())
                        .build())
                .collect(Collectors.toList());

        List<PrescriptionResponse> prescriptions = prescriptionRepository.findByPatientId(patientId).stream()
                .map(p -> PrescriptionResponse.builder()
                        .id(p.getId())
                        .patientId(p.getPatientId())
                        .doctorId(p.getDoctorId())
                        .consultationId(p.getConsultationId())
                        .prescriptionDate(p.getPrescriptionDate())
                        .notes(p.getNotes())
                        .items(p.getItems().stream().map(i -> PrescriptionResponse.PrescriptionItemResponse.builder()
                                .id(i.getId())
                                .medicineId(i.getMedicineId())
                                .medicineName(i.getMedicineName())
                                .dosage(i.getDosage())
                                .frequency(i.getFrequency())
                                .duration(i.getDuration())
                                .instructions(i.getInstructions())
                                .build()).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        List<AdmissionResponse> admissions = admissionRepository.findByPatientId(patientId).stream()
                .map(adm -> AdmissionResponse.builder()
                        .id(adm.getId())
                        .patientId(adm.getPatientId())
                        .doctorId(adm.getDoctorId())
                        .wardId(adm.getWardId())
                        .bedId(adm.getBedId())
                        .admissionDate(adm.getAdmissionDate())
                        .reason(adm.getReason())
                        .diagnosis(adm.getDiagnosis())
                        .status(adm.getStatus())
                        .dischargeDate(adm.getDischargeDate())
                        .build())
                .collect(Collectors.toList());

        List<InvoiceResponse> bills = invoiceRepository.findByPatientId(patientId).stream()
                .map(inv -> InvoiceResponse.builder()
                        .id(inv.getId())
                        .patientId(inv.getPatientId())
                        .invoiceNumber(inv.getInvoiceNumber())
                        .invoiceDate(inv.getInvoiceDate())
                        .totalAmount(inv.getTotalAmount())
                        .paidAmount(inv.getPaidAmount())
                        .balanceAmount(inv.getBalanceAmount())
                        .status(inv.getStatus())
                        .items(inv.getItems().stream().map(item -> InvoiceResponse.InvoiceItemResponse.builder()
                                .id(item.getId())
                                .serviceType(item.getServiceType())
                                .description(item.getDescription())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .build()).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        List<PharmacySaleResponse> pharmacyPurchases = pharmacySaleRepository.findByPatientId(patientId).stream()
                .map(sale -> PharmacySaleResponse.builder()
                        .id(sale.getId())
                        .patientId(sale.getPatientId())
                        .prescriptionId(sale.getPrescriptionId())
                        .saleDate(sale.getSaleDate())
                        .totalAmount(sale.getTotalAmount())
                        .paymentMethod(sale.getPaymentMethod())
                        .items(sale.getItems().stream().map(it -> PharmacySaleResponse.PharmacySaleItemResponse.builder()
                                .id(it.getId())
                                .medicineId(it.getMedicineId())
                                .medicineName(it.getMedicineName())
                                .quantity(it.getQuantity())
                                .unitPrice(it.getUnitPrice())
                                .totalPrice(it.getTotalPrice())
                                .build()).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return PatientMedicalHistoryResponse.builder()
                .profile(profile)
                .walkInVisits(walkIns)
                .emergencyCases(emergencies)
                .appointments(appointments)
                .consultations(consultations)
                .prescriptions(prescriptions)
                .admissions(admissions)
                .bills(bills)
                .pharmacyPurchases(pharmacyPurchases)
                .build();
    }

    private PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .patientId(patient.getPatientId())
                .userId(patient.getUserId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .aadhar(patient.getAadhar())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContact(patient.getEmergencyContact())
                .allergies(patient.getAllergies())
                .medicalHistory(patient.getMedicalHistory())
                .createdAt(patient.getCreatedAt())
                .build();
    }
}
