package com.meditrack.repository;

import com.meditrack.model.DoseLog;
import com.meditrack.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoseLogRepository extends JpaRepository<DoseLog, Long> {
    List<DoseLog> findByMedicine(Medicine medicine);
    List<DoseLog> findByMedicineId(Long medicineId);
}