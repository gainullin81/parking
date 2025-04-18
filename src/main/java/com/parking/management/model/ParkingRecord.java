package com.parking.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String carNumber;

    @Column(nullable = false)
    private String transportType; // Тип транспорта (легковой, грузовой и т.д.)

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private boolean paid;

    @Column(length = 1000)
    private String notes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parking_record_id")
    private List<Photo> photos = new ArrayList<>();
}