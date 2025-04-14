package com.parking.controller;

import com.parking.model.ParkingRecord;
import com.parking.service.ParkingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "http://localhost:3000")
public class ParkingRecordController {
    private final ParkingRecordService service;

    @Autowired
    public ParkingRecordController(ParkingRecordService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParkingRecord> getAllRecords() {
        return service.getAllRecords();
    }

    @GetMapping("/{id}")
    public ParkingRecord getRecordById(@PathVariable Long id) {
        return service.getRecordById(id);
    }

    @PostMapping
    public ParkingRecord createRecord(@RequestPart("record") ParkingRecord record,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return service.createRecord(record, photo);
    }

    @PutMapping("/{id}")
    public ParkingRecord updateRecord(@PathVariable Long id, @RequestBody ParkingRecord record) {
        return service.updateRecord(id, record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/cost")
    public double calculateCost(@PathVariable Long id) {
        return service.calculateCost(id);
    }

    @GetMapping("/search")
    public List<ParkingRecord> searchRecords(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Boolean isPaid) {
        return service.searchRecords(licensePlate, ownerName, startDate, endDate, isPaid);
    }
}