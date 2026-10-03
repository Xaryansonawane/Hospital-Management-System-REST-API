package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.EmergencyCase;
import com.lifecare.hospital.entity.EmergencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmergencyCaseRepository extends JpaRepository<EmergencyCase, Long> {

    @Query("SELECT e FROM EmergencyCase e WHERE e.status IN :statuses ORDER BY " +
           "CASE e.priority WHEN 'CRITICAL' THEN 1 WHEN 'HIGH' THEN 2 WHEN 'MEDIUM' THEN 3 WHEN 'LOW' THEN 4 ELSE 5 END ASC, " +
           "e.arrivalTime ASC")
    List<EmergencyCase> findActiveCasesOrdered(@Param("statuses") List<EmergencyStatus> statuses);

    List<EmergencyCase> findByPatientId(Long patientId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
