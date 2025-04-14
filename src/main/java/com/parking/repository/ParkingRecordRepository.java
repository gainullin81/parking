package com.parking.repository;

import com.parking.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {
    List<ParkingRecord> findByLicensePlateContaining(String licensePlate);

    List<ParkingRecord> findByOwnerNameContaining(String ownerName);

    List<ParkingRecord> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<ParkingRecord> findByIsPaid(boolean isPaid);
}