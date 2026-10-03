package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.response.BedResponse;
import com.lifecare.hospital.entity.Bed;
import com.lifecare.hospital.entity.BedStatus;

import java.util.List;

public interface BedService {
    List<BedResponse> getAllBeds();
    List<BedResponse> getAvailableBeds();
    BedResponse getBedById(Long id);
    BedResponse updateBedStatus(Long id, BedStatus status);
    Bed createBed(Long wardId, String bedNumber);
}
