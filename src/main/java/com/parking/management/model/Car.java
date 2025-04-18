package com.parking.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String carNumber;

    @Column(nullable = false)
    private String parkingNumber;

    @Column(nullable = false)
    private String parkingName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();
}