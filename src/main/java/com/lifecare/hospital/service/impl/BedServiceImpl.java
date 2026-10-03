package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.response.BedResponse;
import com.lifecare.hospital.entity.Bed;
import com.lifecare.hospital.entity.BedStatus;
import com.lifecare.hospital.entity.Ward;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.BedRepository;
import com.lifecare.hospital.repository.WardRepository;
import com.lifecare.hospital.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final WardRepository wardRepository;

    @Override
    public List<BedResponse> getAllBeds() {
        return bedRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BedResponse> getAvailableBeds() {
        return bedRepository.findByStatus(BedStatus.AVAILABLE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BedResponse getBedById(Long id) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found with id: " + id));
        return mapToResponse(bed);
    }

    @Override
    @Transactional
    public BedResponse updateBedStatus(Long id, BedStatus status) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found with id: " + id));
        bed.setStatus(status);
        bed = bedRepository.save(bed);
        return mapToResponse(bed);
    }

    @Override
    @Transactional
    public Bed createBed(Long wardId, String bedNumber) {
        Ward ward = wardRepository.findById(wardId)
                .orElseThrow(() -> new ResourceNotFoundException("Ward not found with id: " + wardId));

        Bed bed = Bed.builder()
                .wardId(ward.getId())
                .bedNumber(bedNumber)
                .status(BedStatus.AVAILABLE)
                .build();

        return bedRepository.save(bed);
    }

    private BedResponse mapToResponse(Bed bed) {
        String wardName = wardRepository.findById(bed.getWardId())
                .map(Ward::getName)
                .orElse("General");

        return BedResponse.builder()
                .id(bed.getId())
                .wardId(bed.getWardId())
                .wardName(wardName)
                .bedNumber(bed.getBedNumber())
                .status(bed.getStatus())
                .build();
    }
}
