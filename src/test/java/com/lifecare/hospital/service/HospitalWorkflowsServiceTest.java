package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.*;
import com.lifecare.hospital.dto.response.*;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.BedUnavailableException;
import com.lifecare.hospital.exception.DoubleBookingException;
import com.lifecare.hospital.exception.ExpiredMedicineException;
import com.lifecare.hospital.exception.InsufficientStockException;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HospitalWorkflowsServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private WalkInVisitRepository walkInVisitRepository;
    @InjectMocks
    private WalkInServiceImpl walkInService;

    @Mock
    private EmergencyCaseRepository emergencyCaseRepository;
    @InjectMocks
    private EmergencyServiceImpl emergencyService;

    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private PharmacySaleRepository pharmacySaleRepository;
    @Mock
    private PrescriptionRepository prescriptionRepository;
    @InjectMocks
    private PharmacyServiceImpl pharmacyService;

    @Mock
    private BedRepository bedRepository;
    @Mock
    private WardRepository wardRepository;
    @Mock
    private AdmissionRepository admissionRepository;
    @InjectMocks
    private AdmissionServiceImpl admissionService;

    private Patient testPatient;
    private Doctor testDoctor;
    private Bed testBed;
    private Medicine testMedicine;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1L)
                .patientId("PAT-2026-00001")
                .firstName("Alice")
                .lastName("Walker")
                .email("alice@example.com")
                .phone("9876543210")
                .build();

        testDoctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .department("Cardiology")
                .status(DoctorStatus.AVAILABLE)
                .build();

        testBed = Bed.builder()
                .id(1L)
                .wardId(1L)
                .bedNumber("GW-01")
                .status(BedStatus.AVAILABLE)
                .build();

        testMedicine = Medicine.builder()
                .id(1L)
                .medicineName("Paracetamol 500mg")
                .quantity(100)
                .minimumStockLevel(10)
                .sellingPrice(new BigDecimal("3.00"))
                .purchasePrice(new BigDecimal("1.50"))
                .expiryDate(LocalDate.now().plusYears(1))
                .build();
    }

    // ==========================================
    // 1. Appointment cannot be double booked
    // ==========================================
    @Test
    @DisplayName("Test 1: Appointment cannot be double booked for same doctor and slot")
    void appointmentCannotBeDoubleBooked() {
        LocalDate date = LocalDate.now().plusDays(2);
        LocalTime time = LocalTime.of(10, 0);

        CreateAppointmentRequest request = CreateAppointmentRequest.builder()
                .patientId(1L)
                .doctorId(1L)
                .appointmentDate(date)
                .appointmentTime(time)
                .reason("Chest pain checkup")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(appointmentRepository.existsConflictingAppointment(1L, date, time)).thenReturn(true);

        assertThrows(DoubleBookingException.class, () -> {
            appointmentService.createAppointment(request, null);
        });

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    // ==========================================
    // 2. Walk-in receives unique token
    // ==========================================
    @Test
    @DisplayName("Test 2: Walk-in receives unique sequential token (e.g. W-001, W-002)")
    void walkInReceivesUniqueToken() {
        CreateWalkInRequest request = CreateWalkInRequest.builder()
                .patientId(1L)
                .visitReason("Fever and cold")
                .priority(Priority.HIGH)
                .build();

        WalkInVisit lastVisit = WalkInVisit.builder().id(5L).tokenNumber("W-005").build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(walkInVisitRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(lastVisit));
        when(walkInVisitRepository.findByTokenNumberAndStatusIn(eq("W-006"), anyList())).thenReturn(Optional.empty());
        when(walkInVisitRepository.save(any(WalkInVisit.class))).thenAnswer(invocation -> {
            WalkInVisit v = invocation.getArgument(0);
            v.setId(6L);
            return v;
        });

        WalkInResponse response = walkInService.createWalkIn(request);

        assertNotNull(response);
        assertEquals("W-006", response.getTokenNumber());
        assertEquals(Priority.HIGH, response.getPriority());
        assertEquals(WalkInStatus.WAITING, response.getStatus());
        verify(walkInVisitRepository, times(1)).save(any(WalkInVisit.class));
    }

    // ==========================================
    // 3. Emergency case can be assigned priority
    // ==========================================
    @Test
    @DisplayName("Test 3: Emergency case can be assigned triage priority")
    void emergencyCaseCanBeAssignedPriority() {
        CreateEmergencyRequest request = CreateEmergencyRequest.builder()
                .patientId(1L)
                .chiefComplaint("Severe trauma after motor vehicle accident")
                .priority(EmergencyPriority.CRITICAL)
                .bloodPressure("80/50")
                .heartRate(130)
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(emergencyCaseRepository.save(any(EmergencyCase.class))).thenAnswer(invocation -> {
            EmergencyCase ec = invocation.getArgument(0);
            ec.setId(10L);
            return ec;
        });

        EmergencyResponse response = emergencyService.createEmergencyCase(request);

        assertNotNull(response);
        assertEquals(EmergencyPriority.CRITICAL, response.getPriority());
        assertEquals(EmergencyStatus.REGISTERED, response.getStatus());
        assertEquals("80/50", response.getBloodPressure());
    }

    // ==========================================
    // 4. Critical emergency appears first
    // ==========================================
    @Test
    @DisplayName("Test 4: Critical emergency cases appear first in the active queue")
    void criticalEmergencyAppearsFirst() {
        EmergencyCase criticalCase = EmergencyCase.builder()
                .id(1L)
                .patientId(1L)
                .priority(EmergencyPriority.CRITICAL)
                .status(EmergencyStatus.TRIAGE)
                .arrivalTime(LocalDateTime.now())
                .chiefComplaint("Cardiac arrest")
                .build();

        EmergencyCase lowCase = EmergencyCase.builder()
                .id(2L)
                .patientId(1L)
                .priority(EmergencyPriority.LOW)
                .status(EmergencyStatus.REGISTERED)
                .arrivalTime(LocalDateTime.now().minusMinutes(20))
                .chiefComplaint("Minor ankle sprain")
                .build();

        // Repo ordered query returns critical first
        when(emergencyCaseRepository.findActiveCasesOrdered(anyList()))
                .thenReturn(Arrays.asList(criticalCase, lowCase));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        List<EmergencyResponse> activeCases = emergencyService.getActiveCases();

        assertEquals(2, activeCases.size());
        assertEquals(EmergencyPriority.CRITICAL, activeCases.get(0).getPriority());
        assertEquals(EmergencyPriority.LOW, activeCases.get(1).getPriority());
    }

    // ==========================================
    // 5. Pharmacy cannot sell unavailable medicine
    // ==========================================
    @Test
    @DisplayName("Test 5: Pharmacy cannot sell medicine with insufficient inventory")
    void pharmacyCannotSellUnavailableMedicine() {
        testMedicine.setQuantity(5); // Only 5 in stock

        CreatePharmacySaleRequest request = CreatePharmacySaleRequest.builder()
                .patientId(1L)
                .paymentMethod(PaymentMethod.CASH)
                .items(List.of(
                        CreatePharmacySaleRequest.SaleItemRequest.builder()
                                .medicineId(1L)
                                .quantity(10) // Requesting 10
                                .build()
                ))
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(testMedicine));

        assertThrows(InsufficientStockException.class, () -> {
            pharmacyService.processSale(request);
        });

        // Verify stock was not decremented
        assertEquals(5, testMedicine.getQuantity());
        verify(pharmacySaleRepository, never()).save(any());
    }

    // ==========================================
    // 6. Expired medicine cannot be sold
    // ==========================================
    @Test
    @DisplayName("Test 6: Expired medicine cannot be sold")
    void expiredMedicineCannotBeSold() {
        testMedicine.setExpiryDate(LocalDate.now().minusDays(5)); // Expired 5 days ago

        CreatePharmacySaleRequest request = CreatePharmacySaleRequest.builder()
                .patientId(1L)
                .paymentMethod(PaymentMethod.CARD)
                .items(List.of(
                        CreatePharmacySaleRequest.SaleItemRequest.builder()
                                .medicineId(1L)
                                .quantity(1)
                                .build()
                ))
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(medicineRepository.findById(1L)).thenReturn(Optional.of(testMedicine));

        assertThrows(ExpiredMedicineException.class, () -> {
            pharmacyService.processSale(request);
        });

        verify(pharmacySaleRepository, never()).save(any());
    }

    // ==========================================
    // 7. Admission cannot use occupied bed
    // ==========================================
    @Test
    @DisplayName("Test 7: Admission cannot use an already occupied bed")
    void admissionCannotUseOccupiedBed() {
        testBed.setStatus(BedStatus.OCCUPIED); // Bed already occupied

        CreateAdmissionRequest request = CreateAdmissionRequest.builder()
                .patientId(1L)
                .doctorId(1L)
                .wardId(1L)
                .bedId(1L)
                .reason("Pneumonia observation")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(bedRepository.findById(1L)).thenReturn(Optional.of(testBed));

        assertThrows(BedUnavailableException.class, () -> {
            admissionService.admitPatient(request);
        });

        verify(admissionRepository, never()).save(any());
    }

    // ==========================================
    // 8. Discharge releases bed
    // ==========================================
    @Test
    @DisplayName("Test 8: Discharging an inpatient resets bed status to AVAILABLE")
    void dischargeReleasesBed() {
        testBed.setStatus(BedStatus.OCCUPIED);

        Admission admission = Admission.builder()
                .id(1L)
                .patientId(1L)
                .doctorId(1L)
                .wardId(1L)
                .bedId(1L)
                .status(AdmissionStatus.ADMITTED)
                .admissionDate(LocalDateTime.now().minusDays(3))
                .build();

        when(admissionRepository.findById(1L)).thenReturn(Optional.of(admission));
        when(admissionRepository.save(any(Admission.class))).thenAnswer(i -> i.getArgument(0));
        when(bedRepository.findById(1L)).thenReturn(Optional.of(testBed));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(wardRepository.findById(1L)).thenReturn(Optional.of(Ward.builder().name("General Ward").build()));

        AdmissionResponse response = admissionService.dischargePatient(1L);

        assertNotNull(response);
        assertEquals(AdmissionStatus.DISCHARGED, response.getStatus());
        assertNotNull(response.getDischargeDate());
        assertEquals(BedStatus.AVAILABLE, testBed.getStatus());
        verify(bedRepository, times(1)).save(testBed);
    }
}
