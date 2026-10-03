package com.lifecare.hospital.config;

import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryRepository inventoryRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initDepartments();
        initAdmin();
        initDoctors();
        initWardsAndBeds();
        initMedicines();
        initSamplePatient();
    }

    private void initDepartments() {
        if (departmentRepository.count() == 0) {
            List<String> departments = List.of(
                    "General Medicine", "Cardiology", "Orthopedics", "Pediatrics",
                    "Dermatology", "Neurology", "Gynecology", "Emergency",
                    "Radiology", "Pathology", "Pharmacy", "ENT", "Oncology", "Physical Therapy"
            );
            for (String deptName : departments) {
                departmentRepository.save(Department.builder()
                        .name(deptName)
                        .description(deptName + " Department")
                        .status("ACTIVE")
                        .build());
            }
            log.info("Initialized default departments.");
        }
    }

    private void initAdmin() {
        if (userRepository.findByEmail("admin@hms.com").isEmpty()) {
            User admin = User.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .email("admin@hms.com")
                    .phone("9876543210")
                    .aadhar("123456789012")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .gender("Male")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Initialized default administrator: admin@hms.com / Admin@123");
        }
    }

    private void initDoctors() {
        if (doctorRepository.count() == 0) {
            createDoctorUser("John", "Doe", "john.doe@lifecare.com", "Pediatrics", "Senior Pediatrician", 12, new BigDecimal("600.00"));
            createDoctorUser("Sarah", "Smith", "sarah.smith@lifecare.com", "Cardiology", "Cardiothoracic Specialist", 15, new BigDecimal("900.00"));
            createDoctorUser("David", "Brown", "david.brown@lifecare.com", "Orthopedics", "Joint Replacement Surgeon", 10, new BigDecimal("750.00"));
            createDoctorUser("Emily", "Johnson", "emily.j@lifecare.com", "Neurology", "Consultant Neurologist", 8, new BigDecimal("700.00"));
            createDoctorUser("Michael", "Clark", "michael.c@lifecare.com", "Emergency", "Emergency Medicine Specialist", 7, new BigDecimal("650.00"));
            log.info("Initialized sample doctors.");
        }
    }

    private void createDoctorUser(String first, String last, String email, String dept, String spec, int exp, BigDecimal fee) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = User.builder()
                    .firstName(first)
                    .lastName(last)
                    .email(email)
                    .phone("9988776655")
                    .aadhar("987654321098")
                    .dateOfBirth(LocalDate.of(1985, 5, 15))
                    .gender("Male")
                    .password(passwordEncoder.encode("Doctor@123"))
                    .role(Role.DOCTOR)
                    .department(dept)
                    .doctorAvatar("/docHolder.jpg")
                    .build();
            user = userRepository.save(user);

            Doctor doctor = Doctor.builder()
                    .userId(user.getId())
                    .firstName(first)
                    .lastName(last)
                    .specialization(spec)
                    .department(dept)
                    .phone(user.getPhone())
                    .email(email)
                    .experience(exp)
                    .consultationFee(fee)
                    .availability("Mon-Sat 09:00 - 17:00")
                    .status(DoctorStatus.AVAILABLE)
                    .doctorAvatar(user.getDoctorAvatar())
                    .build();
            doctorRepository.save(doctor);
        }
    }

    private void initWardsAndBeds() {
        if (wardRepository.count() == 0) {
            createWardWithBeds("General Ward", 10);
            createWardWithBeds("ICU", 5);
            createWardWithBeds("Emergency", 8);
            createWardWithBeds("Private Room", 4);
            log.info("Initialized default hospital wards and beds.");
        }
    }

    private void createWardWithBeds(String wardName, int bedCount) {
        Ward ward = wardRepository.save(Ward.builder().name(wardName).build());
        for (int i = 1; i <= bedCount; i++) {
            String prefix = wardName.replaceAll("\\s+", "").substring(0, Math.min(3, wardName.length())).toUpperCase();
            bedRepository.save(Bed.builder()
                    .wardId(ward.getId())
                    .bedNumber(prefix + "-" + String.format("%02d", i))
                    .status(BedStatus.AVAILABLE)
                    .build());
        }
    }

    private void initMedicines() {
        if (medicineRepository.count() == 0) {
            createMedicine("Paracetamol 500mg", "Analgesic", "GSK", "BATCH-01", LocalDate.now().plusYears(2), new BigDecimal("1.50"), new BigDecimal("3.00"), 500, 50);
            createMedicine("Amoxicillin 500mg", "Antibiotic", "Pfizer", "BATCH-02", LocalDate.now().plusYears(1), new BigDecimal("4.00"), new BigDecimal("8.00"), 200, 30);
            createMedicine("Ibuprofen 400mg", "NSAID", "Abbott", "BATCH-03", LocalDate.now().plusMonths(18), new BigDecimal("2.00"), new BigDecimal("4.50"), 350, 40);
            createMedicine("Cetirizine 10mg", "Antihistamine", "Cipla", "BATCH-04", LocalDate.now().plusYears(2), new BigDecimal("1.00"), new BigDecimal("2.50"), 400, 35);
            createMedicine("Omeprazole 20mg", "Antacid", "Sun Pharma", "BATCH-05", LocalDate.now().plusMonths(15), new BigDecimal("3.50"), new BigDecimal("7.00"), 150, 25);
            createMedicine("LowStock Aspirin", "Analgesic", "Bayer", "BATCH-06", LocalDate.now().plusYears(1), new BigDecimal("2.00"), new BigDecimal("5.00"), 8, 20); // Low stock test
            createMedicine("Expired Syrups", "Cough", "Generic", "BATCH-EX", LocalDate.now().minusDays(30), new BigDecimal("5.00"), new BigDecimal("12.00"), 50, 10); // Expired test
            log.info("Initialized default hospital pharmacy inventory.");
        }
    }

    private void createMedicine(String name, String cat, String mfr, String batch, LocalDate expiry, BigDecimal cost, BigDecimal price, int qty, int minStock) {
        Medicine m = medicineRepository.save(Medicine.builder()
                .medicineName(name)
                .category(cat)
                .manufacturer(mfr)
                .batchNumber(batch)
                .expiryDate(expiry)
                .purchasePrice(cost)
                .sellingPrice(price)
                .quantity(qty)
                .minimumStockLevel(minStock)
                .prescriptionRequired(false)
                .build());

        inventoryRepository.save(Inventory.builder()
                .medicineId(m.getId())
                .quantityAvailable(qty)
                .location("Aisle 1")
                .build());
    }

    private void initSamplePatient() {
        if (patientRepository.count() == 0) {
            User patientUser = User.builder()
                    .firstName("Jane")
                    .lastName("Doe")
                    .email("jane.doe@example.com")
                    .phone("9123456780")
                    .aadhar("112233445566")
                    .dateOfBirth(LocalDate.of(1995, 8, 20))
                    .gender("Female")
                    .password(passwordEncoder.encode("Patient@123"))
                    .role(Role.PATIENT)
                    .build();
            patientUser = userRepository.save(patientUser);

            Patient patient = Patient.builder()
                    .patientId("PAT-2026-00001")
                    .userId(patientUser.getId())
                    .firstName("Jane")
                    .lastName("Doe")
                    .dateOfBirth(LocalDate.of(1995, 8, 20))
                    .gender("Female")
                    .phone("9123456780")
                    .email("jane.doe@example.com")
                    .address("123 Healthcare Ave, Springfield")
                    .bloodGroup("O+")
                    .emergencyContact("John Doe (9123456781)")
                    .allergies("Penicillin")
                    .medicalHistory("Mild asthma")
                    .build();
            patientRepository.save(patient);
            log.info("Initialized sample patient: jane.doe@example.com / Patient@123");
        }
    }
}
