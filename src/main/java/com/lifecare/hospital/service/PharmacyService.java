package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.CreateMedicineRequest;
import com.lifecare.hospital.dto.request.CreatePharmacySaleRequest;
import com.lifecare.hospital.dto.response.MedicineResponse;
import com.lifecare.hospital.dto.response.PharmacySaleResponse;
import com.lifecare.hospital.entity.Medicine;
import com.lifecare.hospital.entity.PaymentMethod;

import java.util.List;

public interface PharmacyService {
    MedicineResponse createMedicine(CreateMedicineRequest request);
    List<MedicineResponse> getAllMedicines();
    MedicineResponse getMedicineById(Long id);
    MedicineResponse updateMedicine(Long id, CreateMedicineRequest request);
    void deleteMedicine(Long id);

    List<MedicineResponse> getLowStockMedicines();
    List<MedicineResponse> getExpiredMedicines();

    PharmacySaleResponse processSale(CreatePharmacySaleRequest request);
    List<PharmacySaleResponse> getAllSales();
    PharmacySaleResponse getSaleById(Long id);
    PharmacySaleResponse dispensePrescription(Long prescriptionId, PaymentMethod paymentMethod);
}
