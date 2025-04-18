package com.parking.management.controller;

import com.parking.management.model.Car;
import com.parking.management.model.OwnerType;
import com.parking.management.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:3000")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> searchCars(
            @RequestParam(required = false) String carNumber,
            @RequestParam(required = false) String parkingNumber,
            @RequestParam(required = false) String parkingName,
            @RequestParam(required = false) OwnerType ownerType,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) Boolean hasPhotos) {
        List<Car> cars = carService.searchCars(
                carNumber, parkingNumber, parkingName, ownerType,
                ownerName, startDate, null, hasPhotos);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }
}