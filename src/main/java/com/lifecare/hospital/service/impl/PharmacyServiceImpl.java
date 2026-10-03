package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.CreateMedicineRequest;
import com.lifecare.hospital.dto.request.CreatePharmacySaleRequest;
import com.lifecare.hospital.dto.response.MedicineResponse;
import com.lifecare.hospital.dto.response.PharmacySaleResponse;
import com.lifecare.hospital.entity.*;
import com.lifecare.hospital.exception.ExpiredMedicineException;
import com.lifecare.hospital.exception.InsufficientStockException;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.*;
import com.lifecare.hospital.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final MedicineRepository medicineRepository;
    private final InventoryRepository inventoryRepository;
    private final PharmacySaleRepository pharmacySaleRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional
    public MedicineResponse createMedicine(CreateMedicineRequest request) {
        Medicine medicine = Medicine.builder()
                .medicineName(request.getMedicineName())
                .category(request.getCategory())
                .manufacturer(request.getManufacturer())
                .batchNumber(request.getBatchNumber())
                .expiryDate(request.getExpiryDate())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .quantity(request.getQuantity())
                .minimumStockLevel(request.getMinimumStockLevel())
                .prescriptionRequired(request.getPrescriptionRequired() != null ? request.getPrescriptionRequired() : false)
                .build();

        medicine = medicineRepository.save(medicine);

        // Synchronize Inventory record
        Inventory inventory = Inventory.builder()
                .medicineId(medicine.getId())
                .quantityAvailable(medicine.getQuantity())
                .location("Central Pharmacy")
                .build();
        inventoryRepository.save(inventory);

        return mapToMedicineResponse(medicine);
    }

    @Override
    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAll().stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return mapToMedicineResponse(medicine);
    }

    @Override
    @Transactional
    public MedicineResponse updateMedicine(Long id, CreateMedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        if (request.getMedicineName() != null) medicine.setMedicineName(request.getMedicineName());
        if (request.getCategory() != null) medicine.setCategory(request.getCategory());
        if (request.getManufacturer() != null) medicine.setManufacturer(request.getManufacturer());
        if (request.getBatchNumber() != null) medicine.setBatchNumber(request.getBatchNumber());
        if (request.getExpiryDate() != null) medicine.setExpiryDate(request.getExpiryDate());
        if (request.getPurchasePrice() != null) medicine.setPurchasePrice(request.getPurchasePrice());
        if (request.getSellingPrice() != null) medicine.setSellingPrice(request.getSellingPrice());
        if (request.getQuantity() != null) {
            medicine.setQuantity(request.getQuantity());
            inventoryRepository.findByMedicineId(id).ifPresent(inv -> {
                inv.setQuantityAvailable(request.getQuantity());
                inventoryRepository.save(inv);
            });
        }
        if (request.getMinimumStockLevel() != null) medicine.setMinimumStockLevel(request.getMinimumStockLevel());
        if (request.getPrescriptionRequired() != null) medicine.setPrescriptionRequired(request.getPrescriptionRequired());

        medicine = medicineRepository.save(medicine);
        return mapToMedicineResponse(medicine);
    }

    @Override
    @Transactional
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        inventoryRepository.findByMedicineId(id).ifPresent(inventoryRepository::delete);
        medicineRepository.delete(medicine);
    }

    @Override
    public List<MedicineResponse> getLowStockMedicines() {
        return medicineRepository.findLowStockMedicines().stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineResponse> getExpiredMedicines() {
        return medicineRepository.findByExpiryDateBefore(LocalDate.now()).stream()
                .map(this::mapToMedicineResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PharmacySaleResponse processSale(CreatePharmacySaleRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        PharmacySale sale = PharmacySale.builder()
                .patientId(patient.getId())
                .prescriptionId(request.getPrescriptionId())
                .saleDate(LocalDateTime.now())
                .paymentMethod(request.getPaymentMethod())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal runningTotal = BigDecimal.ZERO;

        for (CreatePharmacySaleRequest.SaleItemRequest itemReq : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemReq.getMedicineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + itemReq.getMedicineId()));

            // VALIDATION 1: Expired medicine check
            if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
                throw new ExpiredMedicineException("Expired medicine cannot be sold: " + medicine.getMedicineName() + " (Expired on " + medicine.getExpiryDate() + ")");
            }

            // VALIDATION 2: Insufficient stock check
            if (medicine.getQuantity() < itemReq.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for medicine: " + medicine.getMedicineName() +
                        ". Requested: " + itemReq.getQuantity() + ", Available: " + medicine.getQuantity());
            }

            // ATOMIC STOCK DECREMENT
            medicine.setQuantity(medicine.getQuantity() - itemReq.getQuantity());
            medicineRepository.save(medicine);

            inventoryRepository.findByMedicineId(medicine.getId()).ifPresent(inv -> {
                inv.setQuantityAvailable(medicine.getQuantity());
                inventoryRepository.save(inv);
            });

            BigDecimal itemTotal = medicine.getSellingPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            runningTotal = runningTotal.add(itemTotal);

            PharmacySaleItem saleItem = PharmacySaleItem.builder()
                    .medicineId(medicine.getId())
                    .medicineName(medicine.getMedicineName())
                    .quantity(itemReq.getQuantity())
                    .unitPrice(medicine.getSellingPrice())
                    .totalPrice(itemTotal)
                    .build();

            sale.addItem(saleItem);
        }

        sale.setTotalAmount(runningTotal);
        sale = pharmacySaleRepository.save(sale);

        return mapToSaleResponse(sale);
    }

    @Override
    public List<PharmacySaleResponse> getAllSales() {
        return pharmacySaleRepository.findAll().stream()
                .map(this::mapToSaleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PharmacySaleResponse getSaleById(Long id) {
        PharmacySale sale = pharmacySaleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy sale not found with id: " + id));
        return mapToSaleResponse(sale);
    }

    @Override
    @Transactional
    public PharmacySaleResponse dispensePrescription(Long prescriptionId, PaymentMethod paymentMethod) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));

        List<CreatePharmacySaleRequest.SaleItemRequest> saleItems = new ArrayList<>();
        for (PrescriptionItem pItem : prescription.getItems()) {
            saleItems.add(CreatePharmacySaleRequest.SaleItemRequest.builder()
                    .medicineId(pItem.getMedicineId())
                    .quantity(1) // Default 1 pack/course per prescription item
                    .build());
        }

        CreatePharmacySaleRequest saleRequest = CreatePharmacySaleRequest.builder()
                .patientId(prescription.getPatientId())
                .prescriptionId(prescription.getId())
                .paymentMethod(paymentMethod != null ? paymentMethod : PaymentMethod.CASH)
                .items(saleItems)
                .build();

        return processSale(saleRequest);
    }

    private MedicineResponse mapToMedicineResponse(Medicine m) {
        boolean isExpired = m.getExpiryDate().isBefore(LocalDate.now());
        boolean isLowStock = m.getQuantity() <= m.getMinimumStockLevel();

        return MedicineResponse.builder()
                .id(m.getId())
                .medicineName(m.getMedicineName())
                .category(m.getCategory())
                .manufacturer(m.getManufacturer())
                .batchNumber(m.getBatchNumber())
                .expiryDate(m.getExpiryDate())
                .purchasePrice(m.getPurchasePrice())
                .sellingPrice(m.getSellingPrice())
                .quantity(m.getQuantity())
                .minimumStockLevel(m.getMinimumStockLevel())
                .prescriptionRequired(m.getPrescriptionRequired())
                .isLowStock(isLowStock)
                .isExpired(isExpired)
                .build();
    }

    private PharmacySaleResponse mapToSaleResponse(PharmacySale s) {
        String patientName = patientRepository.findById(s.getPatientId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Unknown");

        List<PharmacySaleResponse.PharmacySaleItemResponse> itemResponses = s.getItems().stream()
                .map(it -> PharmacySaleResponse.PharmacySaleItemResponse.builder()
                        .id(it.getId())
                        .medicineId(it.getMedicineId())
                        .medicineName(it.getMedicineName())
                        .quantity(it.getQuantity())
                        .unitPrice(it.getUnitPrice())
                        .totalPrice(it.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return PharmacySaleResponse.builder()
                .id(s.getId())
                .patientId(s.getPatientId())
                .patientName(patientName)
                .prescriptionId(s.getPrescriptionId())
                .saleDate(s.getSaleDate())
                .totalAmount(s.getTotalAmount())
                .paymentMethod(s.getPaymentMethod())
                .items(itemResponses)
                .build();
    }
}
