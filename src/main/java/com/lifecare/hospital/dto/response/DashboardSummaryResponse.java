package com.lifecare.hospital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private long totalPatients;
    private long todayWalkIns;
    private long todayEmergencyCases;
    private long todayAppointments;
    private long pendingAppointments;
    private long completedConsultations;
    private long admittedPatients;
    private long availableDoctors;
    private long availableBeds;
    private long lowStockMedicines;
    private BigDecimal todayRevenue;
    private BigDecimal monthlyRevenue;
}
