package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Bed;
import com.lifecare.hospital.entity.BedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByStatus(BedStatus status);
    List<Bed> findByWardId(Long wardId);
    List<Bed> findByWardIdAndStatus(Long wardId, BedStatus status);
    Optional<Bed> findByWardIdAndBedNumber(Long wardId, String bedNumber);
    long countByStatus(BedStatus status);
}
