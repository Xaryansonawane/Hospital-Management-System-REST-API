package com.lifecare.hospital.service;

import java.util.Map;

public interface ReportsService {
    Map<String, Object> getDailyPatientsReport();
    Map<String, Object> getWalkInsReport();
    Map<String, Object> getEmergencyReport();
    Map<String, Object> getAppointmentsReport();
    Map<String, Object> getDoctorConsultationsReport();
    Map<String, Object> getPharmacySalesReport();
    Map<String, Object> getRevenueReport();
    Map<String, Object> getAdmissionsReport();
    Map<String, Object> getBedOccupancyReport();
}
