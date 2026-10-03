package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateWalkInRequest;
import com.lifecare.hospital.dto.response.WalkInResponse;
import com.lifecare.hospital.entity.WalkInStatus;

import java.util.List;

public interface WalkInService {
    WalkInResponse createWalkIn(CreateWalkInRequest request);
    List<WalkInResponse> getAllWalkIns();
    List<WalkInResponse> getActiveQueue();
    WalkInResponse getWalkInById(Long id);
    WalkInResponse updateStatus(Long id, WalkInStatus status);
}
