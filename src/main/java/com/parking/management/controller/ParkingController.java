package com.parking.management.controller;

import com.parking.management.model.ParkingRecord;
import com.parking.management.model.Photo;
import com.parking.management.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "http://localhost:3000")
public class ParkingController {
    private final ParkingService parkingService;
    private final List<String> transportTypes = new ArrayList<>(Arrays.asList(
            "легковой", "грузовой", "мотоцикл", "автобус"));

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping
    public ResponseEntity<ParkingRecord> createParkingRecord(@RequestBody ParkingRecord parkingRecord) {
        return ResponseEntity.ok(parkingService.createParkingRecord(parkingRecord));
    }

    @GetMapping
    public ResponseEntity<List<ParkingRecord>> getAllParkingRecords() {
        return ResponseEntity.ok(parkingService.getAllParkingRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingRecord> getParkingRecord(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getParkingRecord(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParkingRecord>> searchParkingRecords(
            @RequestParam(required = false) String carNumber,
            @RequestParam(required = false) String transportType) {
        try {
            List<ParkingRecord> records;
            if (carNumber != null && !carNumber.isEmpty() && transportType != null && !transportType.isEmpty()) {
                records = parkingService.searchByCarNumberAndTransportType(carNumber, transportType);
            } else if (carNumber != null && !carNumber.isEmpty()) {
                records = parkingService.searchByCarNumber(carNumber);
            } else if (transportType != null && !transportType.isEmpty()) {
                records = parkingService.searchByTransportType(transportType);
            } else {
                records = parkingService.getAllParkingRecords();
            }
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingRecord> updateParkingRecord(
            @PathVariable Long id,
            @RequestBody ParkingRecord parkingRecord) {
        return ResponseEntity.ok(parkingService.updateParkingRecord(id, parkingRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingRecord(@PathVariable Long id) {
        parkingService.deleteParkingRecord(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<Photo> addPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        try {
            return ResponseEntity.ok(parkingService.addPhoto(id, file, description));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{parkingId}/photos/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable Long parkingId,
            @PathVariable Long photoId) {
        try {
            parkingService.deletePhoto(parkingId, photoId);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/transport-types")
    public ResponseEntity<List<String>> getTransportTypes() {
        return ResponseEntity.ok(transportTypes);
    }

    @PostMapping("/transport-types")
    public ResponseEntity<?> addTransportType(@RequestBody String transportType) {
        if (transportType == null || transportType.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Тип транспорта не может быть пустым");
        }

        if (transportTypes.contains(transportType)) {
            return ResponseEntity.badRequest().body("Такой тип транспорта уже существует");
        }

        transportTypes.add(transportType);
        return ResponseEntity.ok("Тип транспорта успешно добавлен");
    }
}