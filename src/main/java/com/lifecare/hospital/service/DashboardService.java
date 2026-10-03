package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.response.AppointmentResponse;
import com.lifecare.hospital.dto.response.DashboardSummaryResponse;
import com.lifecare.hospital.dto.response.EmergencyResponse;
import com.lifecare.hospital.dto.response.WalkInResponse;

import java.util.List;

public interface DashboardService {
    DashboardSummaryResponse getSummary();
    List<WalkInResponse> getWalkInDashboard();
    List<EmergencyResponse> getEmergencyDashboard();
    List<AppointmentResponse> getAppointmentsDashboard();
}
