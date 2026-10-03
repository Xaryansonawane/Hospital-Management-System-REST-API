package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Doctor;
import com.lifecare.hospital.entity.DoctorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findByDepartment(String department);
    List<Doctor> findByStatus(DoctorStatus status);
    Optional<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
    long countByStatus(DoctorStatus status);
}
