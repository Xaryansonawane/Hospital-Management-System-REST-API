package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.WalkInStatus;
import com.lifecare.hospital.entity.WalkInVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalkInVisitRepository extends JpaRepository<WalkInVisit, Long> {
    Optional<WalkInVisit> findTopByOrderByIdDesc();
    
    Optional<WalkInVisit> findByTokenNumberAndStatusIn(String tokenNumber, List<WalkInStatus> statuses);
    
    List<WalkInVisit> findByStatusInOrderByPriorityDescArrivalTimeAsc(List<WalkInStatus> statuses);
    
    List<WalkInVisit> findByPatientId(Long patientId);
    
    List<WalkInVisit> findByDoctorIdAndStatus(Long doctorId, WalkInStatus status);
    
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(w) FROM WalkInVisit w WHERE w.status = 'WAITING' OR w.status = 'CALLED' OR w.status = 'IN_CONSULTATION'")
    long countActiveWalkIns();
}
