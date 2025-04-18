package com.parking.management.repository;

import com.parking.management.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    List<ParkingRecord> findByCarNumberContainingIgnoreCase(String carNumber);

    List<ParkingRecord> findByTransportType(String transportType);

    List<ParkingRecord> findByCarNumberContainingIgnoreCaseAndTransportType(String carNumber, String transportType);
}