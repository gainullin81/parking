package com.parking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "parking_records")
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String parkingSpotNumber;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private double dailyRate = 100.0;

    @Column(nullable = false)
    private boolean isPaid = false;

    @Column
    private String photoPath;
}