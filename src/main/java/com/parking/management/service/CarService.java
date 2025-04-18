package com.parking.management.service;

import com.parking.management.model.Car;
import com.parking.management.model.OwnerType;
import com.parking.management.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, Car car) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобиль не найден"));

        existingCar.setCarNumber(car.getCarNumber());
        existingCar.setParkingNumber(car.getParkingNumber());
        existingCar.setParkingName(car.getParkingName());
        existingCar.setOwnerType(car.getOwnerType());
        existingCar.setOwnerName(car.getOwnerName());
        existingCar.setStartDate(car.getStartDate());

        return carRepository.save(existingCar);
    }

    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автомобиль не найден"));
    }

    public List<Car> searchCars(String carNumber, String parkingNumber, String parkingName,
            OwnerType ownerType, String ownerName, LocalDateTime startDate,
            LocalDateTime endDate, Boolean hasPhotos) {
        List<Car> cars = carRepository.findAll();

        return cars.stream()
                .filter(car -> carNumber == null || car.getCarNumber().toLowerCase().contains(carNumber.toLowerCase()))
                .filter(car -> parkingNumber == null
                        || car.getParkingNumber().toLowerCase().contains(parkingNumber.toLowerCase()))
                .filter(car -> parkingName == null
                        || car.getParkingName().toLowerCase().contains(parkingName.toLowerCase()))
                .filter(car -> ownerType == null || car.getOwnerType() == ownerType)
                .filter(car -> ownerName == null || car.getOwnerName().toLowerCase().contains(ownerName.toLowerCase()))
                .filter(car -> startDate == null || !car.getStartDate().isBefore(startDate))
                .filter(car -> hasPhotos == null || (hasPhotos && !car.getPhotos().isEmpty()))
                .collect(Collectors.toList());
    }
}