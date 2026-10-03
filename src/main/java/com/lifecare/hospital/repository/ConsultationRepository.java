package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatientId(Long patientId);
    List<Consultation> findByDoctorId(Long doctorId);
    long countByConsultationDateBetween(LocalDateTime start, LocalDateTime end);
}
