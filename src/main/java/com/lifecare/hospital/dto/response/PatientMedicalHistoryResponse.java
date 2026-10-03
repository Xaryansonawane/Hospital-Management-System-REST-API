package com.lifecare.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientMedicalHistoryResponse {
    private PatientResponse profile;
    private List<WalkInResponse> walkInVisits;
    private List<EmergencyResponse> emergencyCases;
    private List<AppointmentResponse> appointments;
    private List<ConsultationResponse> consultations;
    private List<PrescriptionResponse> prescriptions;
    private List<AdmissionResponse> admissions;
    private List<InvoiceResponse> bills;
    private List<PharmacySaleResponse> pharmacyPurchases;
}
