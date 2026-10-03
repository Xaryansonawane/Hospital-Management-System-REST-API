package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByMedicineNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM Medicine m WHERE m.quantity <= m.minimumStockLevel")
    List<Medicine> findLowStockMedicines();

    @Query("SELECT COUNT(m) FROM Medicine m WHERE m.quantity <= m.minimumStockLevel")
    long countLowStockMedicines();

    List<Medicine> findByExpiryDateBefore(LocalDate date);

    @Query("SELECT m FROM Medicine m WHERE m.expiryDate BETWEEN :today AND :threshold")
    List<Medicine> findExpiringSoon(@Param("today") LocalDate today, @Param("threshold") LocalDate threshold);
}
