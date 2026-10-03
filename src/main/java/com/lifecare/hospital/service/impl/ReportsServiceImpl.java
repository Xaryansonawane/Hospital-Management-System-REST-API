package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.entity.AdmissionStatus;
import com.lifecare.hospital.entity.AppointmentStatus;
import com.lifecare.hospital.entity.BedStatus;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final PatientRepository patientRepository;
    private final WalkInVisitRepository walkInVisitRepository;
    private final EmergencyCaseRepository emergencyCaseRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationRepository consultationRepository;
    private final PharmacySaleRepository pharmacySaleRepository;
    private final InvoiceRepository invoiceRepository;
    private final AdmissionRepository admissionRepository;
    private final BedRepository bedRepository;

    @Override
    public Map<String, Object> getDailyPatientsReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("date", LocalDate.now());
        report.put("totalRegisteredPatients", patientRepository.count());
        return report;
    }

    @Override
    public Map<String, Object> getWalkInsReport() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        Map<String, Object> report = new HashMap<>();
        report.put("date", LocalDate.now());
        report.put("todayWalkIns", walkInVisitRepository.countByCreatedAtBetween(start, end));
        report.put("activeQueueCount", walkInVisitRepository.countActiveWalkIns());
        return report;
    }

    @Override
    public Map<String, Object> getEmergencyReport() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        Map<String, Object> report = new HashMap<>();
        report.put("date", LocalDate.now());
        report.put("todayEmergencyCases", emergencyCaseRepository.countByCreatedAtBetween(start, end));
        return report;
    }

    @Override
    public Map<String, Object> getAppointmentsReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("date", LocalDate.now());
        report.put("todayAppointments", appointmentRepository.countByAppointmentDate(LocalDate.now()));
        report.put("pendingAppointments", appointmentRepository.countByStatus(AppointmentStatus.PENDING));
        report.put("confirmedAppointments", appointmentRepository.countByStatus(AppointmentStatus.CONFIRMED));
        return report;
    }

    @Override
    public Map<String, Object> getDoctorConsultationsReport() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        Map<String, Object> report = new HashMap<>();
        report.put("date", LocalDate.now());
        report.put("todayCompletedConsultations", consultationRepository.countByConsultationDateBetween(start, end));
        return report;
    }

    @Override
    public Map<String, Object> getPharmacySalesReport() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        BigDecimal totalSales = pharmacySaleRepository.sumTotalSalesBetween(start, end);
        Map<String, Object> report = new HashMap<>();
        report.put("date", LocalDate.now());
        report.put("todayPharmacyRevenue", totalSales != null ? totalSales : BigDecimal.ZERO);
        return report;
    }

    @Override
    public Map<String, Object> getRevenueReport() {
        LocalDateTime startToday = LocalDate.now().atStartOfDay();
        LocalDateTime endToday = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime startMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        BigDecimal todayRev = invoiceRepository.sumRevenueBetween(startToday, endToday);
        BigDecimal monthlyRev = invoiceRepository.sumRevenueBetween(startMonth, endToday);

        Map<String, Object> report = new HashMap<>();
        report.put("todayRevenue", todayRev != null ? todayRev : BigDecimal.ZERO);
        report.put("monthlyRevenue", monthlyRev != null ? monthlyRev : BigDecimal.ZERO);
        return report;
    }

    @Override
    public Map<String, Object> getAdmissionsReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("activeAdmittedPatients", admissionRepository.countByStatus(AdmissionStatus.ADMITTED));
        report.put("dischargedPatients", admissionRepository.countByStatus(AdmissionStatus.DISCHARGED));
        return report;
    }

    @Override
    public Map<String, Object> getBedOccupancyReport() {
        long available = bedRepository.countByStatus(BedStatus.AVAILABLE);
        long occupied = bedRepository.countByStatus(BedStatus.OCCUPIED);
        long maintenance = bedRepository.countByStatus(BedStatus.MAINTENANCE);
        long total = available + occupied + maintenance;
        double rate = total > 0 ? ((double) occupied / total) * 100.0 : 0.0;

        Map<String, Object> report = new HashMap<>();
        report.put("totalBeds", total);
        report.put("availableBeds", available);
        report.put("occupiedBeds", occupied);
        report.put("maintenanceBeds", maintenance);
        report.put("occupancyRatePercentage", Math.round(rate * 10.0) / 10.0);
        return report;
    }
}
