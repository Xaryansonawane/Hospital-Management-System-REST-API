package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.dto.response.DashboardSummaryResponse;
import com.lifecare.hospital.dto.response.EmergencyResponse;
import com.lifecare.hospital.dto.response.WalkInResponse;
import com.lifecare.hospital.entity.AdmissionStatus;
import com.lifecare.hospital.entity.AppointmentStatus;
import com.lifecare.hospital.entity.BedStatus;
import com.lifecare.hospital.entity.DoctorStatus;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.AppointmentService;
import com.lifecare.hospital.service.DashboardService;
import com.lifecare.hospital.service.EmergencyService;
import com.lifecare.hospital.service.WalkInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;
    private final WalkInVisitRepository walkInVisitRepository;
    private final EmergencyCaseRepository emergencyCaseRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final AdmissionRepository admissionRepository;
    private final DoctorRepository doctorRepository;
    private final BedRepository bedRepository;
    private final MedicineRepository medicineRepository;
    private final InvoiceRepository invoiceRepository;
    private final WalkInService walkInService;
    private final EmergencyService emergencyService;
    private final AppointmentService appointmentService;

    @Override
    public DashboardSummaryResponse getSummary() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        long totalPatients = patientRepository.count();
        long todayWalkIns = walkInVisitRepository.countByCreatedAtBetween(startOfToday, endOfToday);
        long todayEmergency = emergencyCaseRepository.countByCreatedAtBetween(startOfToday, endOfToday);
        long todayAppointments = appointmentRepository.countByAppointmentDate(LocalDate.now());
        long pendingAppointments = appointmentRepository.countByStatus(AppointmentStatus.PENDING);
        long completedConsultations = consultationRepository.countByConsultationDateBetween(startOfToday, endOfToday);
        long admittedPatients = admissionRepository.countByStatus(AdmissionStatus.ADMITTED);
        long availableDoctors = doctorRepository.countByStatus(DoctorStatus.AVAILABLE);
        long availableBeds = bedRepository.countByStatus(BedStatus.AVAILABLE);
        long lowStock = medicineRepository.countLowStockMedicines();
        BigDecimal todayRevenue = invoiceRepository.sumRevenueBetween(startOfToday, endOfToday);
        BigDecimal monthlyRevenue = invoiceRepository.sumRevenueBetween(startOfMonth, endOfToday);

        return DashboardSummaryResponse.builder()
                .totalPatients(totalPatients)
                .todayWalkIns(todayWalkIns)
                .todayEmergencyCases(todayEmergency)
                .todayAppointments(todayAppointments)
                .pendingAppointments(pendingAppointments)
                .completedConsultations(completedConsultations)
                .admittedPatients(admittedPatients)
                .availableDoctors(availableDoctors)
                .availableBeds(availableBeds)
                .lowStockMedicines(lowStock)
                .todayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO)
                .monthlyRevenue(monthlyRevenue != null ? monthlyRevenue : BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<WalkInResponse> getWalkInDashboard() {
        return walkInService.getActiveQueue();
    }

    @Override
    public List<EmergencyResponse> getEmergencyDashboard() {
        return emergencyService.getActiveCases();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsDashboard() {
        return appointmentService.getAllAppointments();
    }
}
