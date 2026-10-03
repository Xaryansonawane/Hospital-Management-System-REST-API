package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Admission;
import com.lifecare.hospital.entity.AdmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByPatientId(Long patientId);
    List<Admission> findByStatus(AdmissionStatus status);
    Optional<Admission> findByBedIdAndStatusIn(Long bedId, List<AdmissionStatus> statuses);
    long countByStatus(AdmissionStatus status);
}
