package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findBySupplierId(Long supplierId);
    List<Purchase> findByPurchaseDateBetween(LocalDate start, LocalDate end);
}
