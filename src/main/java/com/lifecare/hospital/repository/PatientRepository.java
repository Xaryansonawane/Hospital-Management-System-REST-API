package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientId(String patientId);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByUserId(Long userId);
    long count();
    Optional<Patient> findTopByOrderByIdDesc();
}
