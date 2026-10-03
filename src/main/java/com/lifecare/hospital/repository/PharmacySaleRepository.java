package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.PharmacySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PharmacySaleRepository extends JpaRepository<PharmacySale, Long> {
    List<PharmacySale> findByPatientId(Long patientId);
    List<PharmacySale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM PharmacySale s WHERE s.saleDate BETWEEN :start AND :end")
    BigDecimal sumTotalSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
