package com.parking.service;

import com.parking.model.ParkingRecord;
import com.parking.repository.ParkingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingRecordService {
    private final ParkingRecordRepository repository;
    private final Path uploadPath = Paths.get("uploads");

    @Autowired
    public ParkingRecordService(ParkingRecordRepository repository) {
        this.repository = repository;
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public List<ParkingRecord> getAllRecords() {
        return repository.findAll();
    }

    public ParkingRecord getRecordById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public ParkingRecord createRecord(ParkingRecord record, MultipartFile photo) {
        if (photo != null && !photo.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(photo.getInputStream(), filePath);
                record.setPhotoPath(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("Could not store the file", e);
            }
        }
        return repository.save(record);
    }

    public ParkingRecord updateRecord(Long id, ParkingRecord record) {
        ParkingRecord existingRecord = getRecordById(id);
        existingRecord.setParkingSpotNumber(record.getParkingSpotNumber());
        existingRecord.setLicensePlate(record.getLicensePlate());
        existingRecord.setOwnerName(record.getOwnerName());
        existingRecord.setStartTime(record.getStartTime());
        existingRecord.setEndTime(record.getEndTime());
        existingRecord.setDailyRate(record.getDailyRate());
        existingRecord.setPaid(record.isPaid());
        return repository.save(existingRecord);
    }

    public void deleteRecord(Long id) {
        repository.deleteById(id);
    }

    public double calculateCost(Long id) {
        ParkingRecord record = getRecordById(id);
        long days = java.time.Duration.between(record.getStartTime(), record.getEndTime()).toDays();
        return days * record.getDailyRate();
    }

    public List<ParkingRecord> searchRecords(String licensePlate, String ownerName,
            LocalDateTime startDate, LocalDateTime endDate,
            Boolean isPaid) {
        if (licensePlate != null) {
            return repository.findByLicensePlateContaining(licensePlate);
        }
        if (ownerName != null) {
            return repository.findByOwnerNameContaining(ownerName);
        }
        if (startDate != null && endDate != null) {
            return repository.findByStartTimeBetween(startDate, endDate);
        }
        if (isPaid != null) {
            return repository.findByIsPaid(isPaid);
        }
        return repository.findAll();
    }
}