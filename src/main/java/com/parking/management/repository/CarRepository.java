package com.parking.management.repository;

import com.parking.management.model.Car;
import com.parking.management.model.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByCarNumberContainingIgnoreCase(String carNumber);

    List<Car> findByParkingNumberContainingIgnoreCase(String parkingNumber);

    List<Car> findByParkingNameContainingIgnoreCase(String parkingName);

    List<Car> findByOwnerType(OwnerType ownerType);

    List<Car> findByOwnerNameContainingIgnoreCase(String ownerName);

    List<Car> findByStartDateBetween(LocalDateTime start, LocalDateTime end);

    List<Car> findByPhotosIsNotNull();
}