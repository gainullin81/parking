package com.parking.management.service;

import com.parking.management.model.ParkingRecord;
import com.parking.management.model.Photo;
import com.parking.management.repository.ParkingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ParkingService {
    private final ParkingRecordRepository parkingRecordRepository;
    private final Path uploadPath = Paths.get("uploads");

    @Autowired
    public ParkingService(ParkingRecordRepository parkingRecordRepository) {
        this.parkingRecordRepository = parkingRecordRepository;
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public ParkingRecord createParkingRecord(ParkingRecord parkingRecord) {
        parkingRecord.setEntryTime(LocalDateTime.now());
        return parkingRecordRepository.save(parkingRecord);
    }

    public List<ParkingRecord> getAllParkingRecords() {
        return parkingRecordRepository.findAll();
    }

    public ParkingRecord getParkingRecord(Long id) {
        return parkingRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking record not found"));
    }

    public List<ParkingRecord> searchByCarNumber(String carNumber) {
        return parkingRecordRepository.findByCarNumberContainingIgnoreCase(carNumber);
    }

    public List<ParkingRecord> searchByTransportType(String transportType) {
        return parkingRecordRepository.findByTransportType(transportType);
    }

    public List<ParkingRecord> searchByCarNumberAndTransportType(String carNumber, String transportType) {
        return parkingRecordRepository.findByCarNumberContainingIgnoreCaseAndTransportType(carNumber, transportType);
    }

    public ParkingRecord updateParkingRecord(Long id, ParkingRecord parkingRecord) {
        ParkingRecord existingRecord = getParkingRecord(id);
        existingRecord.setCarNumber(parkingRecord.getCarNumber());
        existingRecord.setExitTime(parkingRecord.getExitTime());
        existingRecord.setPaid(parkingRecord.isPaid());
        existingRecord.setNotes(parkingRecord.getNotes());
        return parkingRecordRepository.save(existingRecord);
    }

    public void deleteParkingRecord(Long id) {
        ParkingRecord record = getParkingRecord(id);
        // Delete associated photos
        for (Photo photo : record.getPhotos()) {
            try {
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
            } catch (IOException e) {
                // Log error but continue with deletion
                e.printStackTrace();
            }
        }
        parkingRecordRepository.deleteById(id);
    }

    public Photo addPhoto(Long parkingRecordId, MultipartFile file, String description) throws IOException {
        ParkingRecord parkingRecord = getParkingRecord(parkingRecordId);

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        Photo photo = new Photo();
        photo.setFileName(originalFilename);
        photo.setFilePath(filePath.toString());
        photo.setDescription(description);

        parkingRecord.getPhotos().add(photo);
        parkingRecordRepository.save(parkingRecord);

        return photo;
    }

    public void deletePhoto(Long parkingRecordId, Long photoId) throws IOException {
        ParkingRecord parkingRecord = getParkingRecord(parkingRecordId);
        Photo photoToDelete = parkingRecord.getPhotos().stream()
                .filter(photo -> photo.getId().equals(photoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Files.deleteIfExists(Paths.get(photoToDelete.getFilePath()));
        parkingRecord.getPhotos().remove(photoToDelete);
        parkingRecordRepository.save(parkingRecord);
    }
}