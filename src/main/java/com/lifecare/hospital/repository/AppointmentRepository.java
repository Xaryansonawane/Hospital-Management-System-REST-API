package com.lifecare.hospital.repository;

import com.lifecare.hospital.entity.Appointment;
import com.lifecare.hospital.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctorId = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.appointmentTime = :time " +
           "AND a.status NOT IN ('CANCELLED')")
    boolean existsConflictingAppointment(@Param("doctorId") Long doctorId,
                                         @Param("date") LocalDate date,
                                         @Param("time") LocalTime time);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByAppointmentDate(LocalDate date);

    long countByAppointmentDate(LocalDate date);

    long countByStatus(AppointmentStatus status);
}
